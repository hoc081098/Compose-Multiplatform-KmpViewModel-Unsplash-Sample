package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.coroutines_utils.publish
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.requireRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.GetPhotoDetailByIdUseCase
import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlin.jvm.JvmName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class PhotoDetailViewModel(
  savedStateHandle: SavedStateHandle,
  private val getPhotoDetailByIdUseCase: GetPhotoDetailByIdUseCase,
) : ViewModel() {
  private val route = savedStateHandle.requireRoute<PhotoDetailRoute>()

  private val _intentChannel = Channel<PhotoDetailViewIntent>(capacity = 1)

  internal val uiStateFlow: StateFlow<PhotoDetailUiState>

  init {
    Napier.d("init route=$route -> $this", tag = "PhotoDetailViewModel")
    addCloseable { Napier.d("close route=$route -> $this", tag = "PhotoDetailViewModel") }

    uiStateFlow =
      _intentChannel
        .consumeAsFlow()
        .onEach { Napier.d(message = "intent $it", tag = "PhotoDetailViewModel") }
        .publish {
          merge(
            select {
              it
                .filterIsInstance<PhotoDetailViewIntent.Init>()
                .toPartialStateChangesFlow()
            },
            select {
              it
                .filterIsInstance<PhotoDetailViewIntent.Refresh>()
                .toPartialStateChangesFlow()
            },
            select {
              it
                .filterIsInstance<PhotoDetailViewIntent.Retry>()
                .toPartialStateChangesFlow()
            },
          )
        }.scan(PhotoDetailUiState.INITIAL) { state, change -> change.reduce(state) }
        .stateIn(
          scope = viewModelScope,
          started = SharingStarted.Eagerly,
          initialValue = PhotoDetailUiState.INITIAL,
        )
  }

  internal fun process(intent: PhotoDetailViewIntent) {
    viewModelScope.launch { _intentChannel.send(intent) }
  }

  //region View intent processors
  @JvmName("initIntentFlowToPartialStateChangesFlow")
  private fun Flow<PhotoDetailViewIntent.Init>.toPartialStateChangesFlow(): Flow<PhotoDetailPartialStateChange.InitAndRetry> =
    take(1)
      .flatMapConcat {
        flowFromSuspend { getPhotoDetailByIdUseCase(route.id) }
          .map { either ->
            either.fold(
              ifLeft = {
                PhotoDetailPartialStateChange.InitAndRetry.Error(it)
              },
              ifRight = {
                PhotoDetailPartialStateChange.InitAndRetry.Content(it.toPhotoDetailUi())
              },
            )
          }.startWith(PhotoDetailPartialStateChange.InitAndRetry.Loading)
      }

  @JvmName("retryIntentFlowToPartialStateChangesFlow")
  private fun Flow<PhotoDetailViewIntent.Retry>.toPartialStateChangesFlow(): Flow<PhotoDetailPartialStateChange.InitAndRetry> =
    filter { uiStateFlow.value is PhotoDetailUiState.Error }
      .flatMapFirst {
        flowFromSuspend { getPhotoDetailByIdUseCase(route.id) }
          .map { either ->
            either.fold(
              ifLeft = {
                PhotoDetailPartialStateChange.InitAndRetry.Error(it)
              },
              ifRight = {
                PhotoDetailPartialStateChange.InitAndRetry.Content(it.toPhotoDetailUi())
              },
            )
          }.startWith(PhotoDetailPartialStateChange.InitAndRetry.Loading)
      }

  @JvmName("refreshIntentFlowToPartialStateChangesFlow")
  private fun Flow<PhotoDetailViewIntent.Refresh>.toPartialStateChangesFlow(): Flow<PhotoDetailPartialStateChange.Refresh> =
    filter {
      val state = uiStateFlow.value
      state is PhotoDetailUiState.Content && !state.isRefreshing
    }.flatMapFirst {
      flowFromSuspend { getPhotoDetailByIdUseCase(route.id) }
        .map { either ->
          either.fold(
            ifLeft = {
              PhotoDetailPartialStateChange.Refresh.Error(it)
            },
            ifRight = {
              PhotoDetailPartialStateChange.Refresh.Content(it.toPhotoDetailUi())
            },
          )
        }.startWith(PhotoDetailPartialStateChange.Refresh.Refreshing)
    }
  //endregion
}
