package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Immutable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoDetailUi

@Immutable
internal sealed interface PhotoDetailPartialStateChange {
  fun reduce(state: PhotoDetailUiState): PhotoDetailUiState

  sealed interface InitAndRetry : PhotoDetailPartialStateChange {
    data object Loading : InitAndRetry {
      override fun reduce(state: PhotoDetailUiState) = PhotoDetailUiState.Loading
    }

    data class Error(
      val error: PhotoDetailError,
    ) : InitAndRetry {
      override fun reduce(state: PhotoDetailUiState) = PhotoDetailUiState.Error(error)
    }

    data class Content(
      val photoDetail: PhotoDetailUi,
    ) : InitAndRetry {
      override fun reduce(state: PhotoDetailUiState) =
        if (state is PhotoDetailUiState.Content) {
          state.copy(photoDetail = photoDetail)
        } else {
          PhotoDetailUiState.Content(photoDetail = photoDetail, isRefreshing = false)
        }
    }
  }

  sealed interface Refresh : PhotoDetailPartialStateChange {
    data object Refreshing : Refresh {
      override fun reduce(state: PhotoDetailUiState) =
        if (state is PhotoDetailUiState.Content) {
          state.copy(isRefreshing = true)
        } else {
          state
        }
    }

    data class Error(
      val error: PhotoDetailError,
    ) : Refresh {
      override fun reduce(state: PhotoDetailUiState) =
        if (state is PhotoDetailUiState.Content) {
          state.copy(isRefreshing = false)
        } else {
          state
        }
    }

    data class Content(
      val photoDetail: PhotoDetailUi,
    ) : Refresh {
      override fun reduce(state: PhotoDetailUiState) =
        PhotoDetailUiState.Content(
          photoDetail = photoDetail,
          isRefreshing = false,
        )
    }
  }
}
