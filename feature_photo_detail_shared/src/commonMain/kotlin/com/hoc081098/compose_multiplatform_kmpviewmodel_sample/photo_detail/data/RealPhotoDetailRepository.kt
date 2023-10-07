package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import arrow.core.Either
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.coroutines_utils.AppCoroutineDispatchers
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.UnsplashApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoCreator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetail
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailRepository
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoSize
import io.github.aakira.napier.Napier
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    PhotoDetailRepository::class,
  ],
)
internal class RealPhotoDetailRepository(
  private val unsplashApi: UnsplashApi,
  private val photoDetailErrorMapper: PhotoDetailErrorMapper,
  private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : PhotoDetailRepository {
  override suspend fun getPhotoDetailById(id: String) =
    withContext(appCoroutineDispatchers.io) {
      Either
        .catch {
          unsplashApi
            .getPhotoDetailById(id)
            .toPhotoDetail()
        }.onLeft {
          Napier.e(
            throwable = it,
            tag = "RealPhotoDetailRepository",
            message = "getPhotoDetailById($id) failed",
          )
        }.mapLeft(photoDetailErrorMapper)
    }
}

private fun CoverPhotoResponse.toPhotoDetail(): PhotoDetail =
  PhotoDetail(
    id = id,
    fullUrl = urls.full,
    description = description,
    alternativeDescription = altDescription,
    createdAt = createdAt,
    updatedAt = updatedAt,
    promotedAt = promotedAt,
    creator =
      PhotoCreator(
        id = user.id,
        username = user.username,
        name = user.name,
        mediumProfileImageUrl = user.profileImage?.medium,
      ),
    size =
      PhotoSize(
        width = width.toUInt(),
        height = height.toUInt(),
      ),
  )
