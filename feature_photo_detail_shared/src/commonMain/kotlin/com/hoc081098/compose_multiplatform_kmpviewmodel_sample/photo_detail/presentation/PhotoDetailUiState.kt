package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetail
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoCreatorUi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoDetailUi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoSizeUi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.stable_wrappers.ImmutableWrapper
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.stable_wrappers.toImmutableWrapper
import kotlinx.datetime.Instant

@Immutable
internal sealed interface PhotoDetailUiState {
  data object Loading : PhotoDetailUiState

  data class Content(
    val photoDetail: PhotoDetailUi,
    val isRefreshing: Boolean,
  ) : PhotoDetailUiState

  data class Error(
    val error: PhotoDetailError,
  ) : PhotoDetailUiState

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
    val size: PhotoSizeUi,
  )

  @Immutable
  data class PhotoSizeUi(
    val width: UInt,
    val height: UInt,
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
internal fun PhotoDetail.toPhotoDetailUi(): PhotoDetailUi =
  PhotoDetailUi(
    id = id,
    fullUrl = fullUrl,
    description = description,
    alternativeDescription = alternativeDescription,
    createdAt = createdAt.toImmutableWrapper(),
    updatedAt = updatedAt.toImmutableWrapper(),
    promotedAt = promotedAt.toImmutableWrapper(),
    creator =
      PhotoCreatorUi(
        id = creator.id,
        username = creator.username,
        name = creator.name,
        mediumProfileImageUrl = creator.mediumProfileImageUrl,
      ),
    size =
      PhotoSizeUi(
        width = size.width,
        height = size.height,
      ),
  )
