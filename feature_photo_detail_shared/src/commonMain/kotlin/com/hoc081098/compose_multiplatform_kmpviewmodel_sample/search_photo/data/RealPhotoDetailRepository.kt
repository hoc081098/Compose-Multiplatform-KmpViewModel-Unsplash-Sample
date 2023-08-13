package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import arrow.core.Either
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.UnsplashApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.PhotoCreator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.PhotoDetail
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.PhotoDetailRepository
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    PhotoDetailRepository::class,
  ],
)
internal class RealPhotoDetailRepository(
  private val unsplashApi: UnsplashApi,
  private val photoDetailErrorMapper: PhotoDetailErrorMapper,
) : PhotoDetailRepository {
  override suspend fun getPhotoDetailById(id: String) = Either.catch {
    unsplashApi
      .getPhotoDetailById(id)
      .toPhotoDetail()
  }.mapLeft(photoDetailErrorMapper)
}

private fun CoverPhotoResponse.toPhotoDetail(): PhotoDetail = PhotoDetail(
  id = id,
  fullUrl = urls.full,
  description = description,
  alternativeDescription = altDescription,
  createdAt = createdAt,
  updatedAt = updatedAt,
  promotedAt = promotedAt,
  creator = PhotoCreator(
    id = user.id,
    username = user.username,
    name = user.name,
    smallProfileImageUrl = user.profileImage.small,
  ),
)
