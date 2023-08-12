package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation

import androidx.compose.runtime.Immutable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.common.ImmutableWrapper
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant

@Immutable
data class SearchPhotoUiState(
  val photoUiItems: ImmutableList<PhotoUiItem>,
  val isLoading: Boolean,
  val error: SearchPhotoError?,
  val submittedTerm: String?,
) {
  @Immutable
  data class PhotoUiItem(
    val id: String,
    val slug: String,
    val createdAt: ImmutableWrapper<Instant>,
    val updatedAt: ImmutableWrapper<Instant>,
    val promotedAt: ImmutableWrapper<Instant?>,
    val width: Int,
    val height: Int,
  )

  companion object {
    val INITIAL = SearchPhotoUiState(
      photoUiItems = persistentListOf(),
      isLoading = false,
      error = null,
      submittedTerm = null,
    )
  }
}