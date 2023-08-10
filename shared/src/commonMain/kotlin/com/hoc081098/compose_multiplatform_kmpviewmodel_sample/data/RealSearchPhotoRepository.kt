package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.domain.CoverPhoto
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.domain.SearchPhotoRepository

internal class RealSearchPhotoRepository(
  private val unsplashApi: UnsplashApi,
) : SearchPhotoRepository {
  override suspend fun search(query: String) = unsplashApi
    .searchPhotos(query)
    .results
    .map(CoverPhotoResponse::toCoverPhoto)
}

private fun CoverPhotoResponse.toCoverPhoto() = CoverPhoto(
  id = id,
  slug = slug,
  createdAt = createdAt,
  updatedAt = updatedAt,
  promotedAt = promotedAt,
  width = width,
  height = height,
)