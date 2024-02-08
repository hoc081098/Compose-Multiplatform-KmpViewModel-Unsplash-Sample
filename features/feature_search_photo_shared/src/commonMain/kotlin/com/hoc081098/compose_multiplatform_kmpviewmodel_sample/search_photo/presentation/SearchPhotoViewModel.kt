package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation

import arrow.core.right
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailScreenRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.CoverPhoto
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoUseCase
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoUiState.PhotoUiItem
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.stable_wrappers.toImmutableWrapper
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.kmp.viewmodel.safe.string
import com.hoc081098.solivagant.navigation.NavEventNavigator
import io.github.aakira.napier.Napier
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Factory
internal class SearchPhotoViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val searchPhotoUseCase: SearchPhotoUseCase,
  private val navigator: NavEventNavigator,
) : ViewModel() {
  val searchTermStateFlow: StateFlow<String> =
    savedStateHandle
      .safe
      .getStateFlow(key = SEARCH_TERM_KEY)

  val stateFlow: StateFlow<SearchPhotoUiState> =
    searchTermStateFlow
      .debounce(400.milliseconds)
      .map { it.orEmpty().trim() }
      .distinctUntilChanged()
      .flatMapLatest(searchPhotoUseCase::executeSearching)
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SearchPhotoUiState.INITIAL,
      )

  init {
    Napier.d(message = "init $this", tag = "SearchPhotoViewModel")
    addCloseable { Napier.d(message = "close $this", tag = "SearchPhotoViewModel") }
  }

  fun search(term: String) = savedStateHandle.safe { it[SEARCH_TERM_KEY] = term }

  fun navigateToPhotoDetail(item: PhotoUiItem) = navigator.navigateTo(PhotoDetailScreenRoute(id = item.id))

  companion object {
    private val SEARCH_TERM_KEY =
      NonNullSavedStateHandleKey.string(
        key = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.search_term",
        defaultValue = "",
      )
  }
}

private fun SearchPhotoUseCase.executeSearching(term: String): Flow<SearchPhotoUiState> =
  flowFromSuspend {
    if (term.isEmpty()) {
      emptyList<CoverPhoto>().right()
    } else {
      invoke(term)
    }
  }.onStart { Napier.d("search products term=$term") }
    .onEach { either ->
      Napier.d(
        "search products ${
          either.fold(
            ifLeft = { "error=$it" },
            ifRight = { "success=${it.size}" },
          )
        }",
      )
    }.map { either ->
      either.fold(
        ifLeft = {
          SearchPhotoUiState(
            isLoading = false,
            error = it,
            submittedTerm = term,
            photoUiItems = persistentListOf(),
          )
        },
        ifRight = { coverPhotos ->
          SearchPhotoUiState(
            photoUiItems =
              coverPhotos
                .map { it.toPhotoUiItem() }
                .toImmutableList(),
            isLoading = false,
            error = null,
            submittedTerm = term,
          )
        },
      )
    }.startWith {
      SearchPhotoUiState(
        isLoading = true,
        error = null,
        submittedTerm = term,
        photoUiItems = persistentListOf(),
      )
    }

private fun CoverPhoto.toPhotoUiItem(): PhotoUiItem =
  PhotoUiItem(
    id = id,
    slug = slug,
    createdAt = createdAt.toImmutableWrapper(),
    updatedAt = updatedAt.toImmutableWrapper(),
    promotedAt = promotedAt.toImmutableWrapper(),
    width = width,
    height = height,
    thumbnailUrl = thumbnailUrl,
  )
