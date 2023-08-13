package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation

import androidx.compose.runtime.Immutable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.ImmutableWrapper
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.PhotoDetailError
import kotlinx.datetime.Instant

@Immutable
sealed interface PhotoDetailUiState {
  data object Loading : PhotoDetailUiState

  data class Success(val photoDetail: PhotoDetailUi) : PhotoDetailUiState

  data class Error(val error: PhotoDetailError) : PhotoDetailUiState

  @Immutable
  data class PhotoDetailUi(
    val id: String,
    val fullUrl: String,
    val description: String?,
    val alternativeDescription: String?,
    val createdAt: ImmutableWrapper<Instant>,
    val updatedAt: ImmutableWrapper<Instant>,
    val promotedAt: ImmutableWrapper<Instant?>,
    val creator: PhotoCreatorUi,
  )

  @Immutable
  data class PhotoCreatorUi(
    val id: String,
    val username: String,
    val name: String,
    val smallProfileImageUrl: String,
  )
}
