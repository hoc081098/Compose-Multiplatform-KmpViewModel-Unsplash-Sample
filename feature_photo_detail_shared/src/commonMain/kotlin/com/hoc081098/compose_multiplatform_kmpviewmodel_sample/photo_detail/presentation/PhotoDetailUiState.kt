package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.ImmutableWrapper
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetail
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoCreatorUi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoDetailUi
import kotlinx.datetime.Instant

@Immutable
internal sealed interface PhotoDetailUiState {
  data object Loading : PhotoDetailUiState

  data class Content(
    val photoDetail: PhotoDetailUi,
    val isRefreshing: Boolean,
  ) : PhotoDetailUiState

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
    val mediumProfileImageUrl: String?,
  )

  companion object {
    val INITIAL: PhotoDetailUiState get() = Loading
  }
}

@Stable
internal fun PhotoDetail.toPhotoDetailUi(): PhotoDetailUi {
  return PhotoDetailUi(
    id = id,
    fullUrl = fullUrl,
    description = description,
    alternativeDescription = alternativeDescription,
    createdAt = ImmutableWrapper(createdAt),
    updatedAt = ImmutableWrapper(updatedAt),
    promotedAt = ImmutableWrapper(promotedAt),
    creator = PhotoCreatorUi(
      id = creator.id,
      username = creator.username,
      name = creator.name,
      mediumProfileImageUrl = creator.mediumProfileImageUrl,
    ),
  )
}