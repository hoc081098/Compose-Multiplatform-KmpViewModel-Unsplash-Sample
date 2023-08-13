package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.GetPhotoDetailByIdUseCase
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class PhotoDetailViewModel(
  savedStateHandle: SavedStateHandle,
  private val getPhotoDetailByIdUseCase: GetPhotoDetailByIdUseCase,
) : ViewModel() {
  internal val id = checkNotNull(savedStateHandle.get<String>(ID_KEY)) {
    """id must not be null.
      |For non-Android platforms, you must set id to `SavedStateHandle` with key $ID_KEY,
      |and pass that `SavedStateHandle` to `PhotoDetailViewModel` constructor.
      |
    """.trimMargin()
  }

  private val _intentSharedFlow = MutableSharedFlow<PhotoDetailViewIntent>(
    replay = 0,
    extraBufferCapacity = 1,
  )

  init {
    Napier.d("init $this")
    addCloseable { Napier.d("close $this") }
  }

  internal fun process(intent: PhotoDetailViewIntent) {
    viewModelScope.launch { _intentSharedFlow.emit(intent) }
  }

  companion object {
    // This key is used by non-Android platforms to set id to SavedStateHandle,
    // used by Android platform to set id to Bundle (handled by Compose-Navigation).
    const val ID_KEY = "id"
  }
}
