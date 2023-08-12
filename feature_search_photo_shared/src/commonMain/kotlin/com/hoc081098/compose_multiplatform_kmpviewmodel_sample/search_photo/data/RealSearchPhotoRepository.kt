package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.CoverPhoto
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoRepository
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    SearchPhotoRepository::class,
  ]
)
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