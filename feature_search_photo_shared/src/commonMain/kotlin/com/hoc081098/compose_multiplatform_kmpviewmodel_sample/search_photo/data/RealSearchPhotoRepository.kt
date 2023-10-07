package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import arrow.core.Either
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.coroutines_utils.AppCoroutineDispatchers
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.UnsplashApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.CoverPhoto
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    SearchPhotoRepository::class,
  ],
)
internal class RealSearchPhotoRepository(
  private val unsplashApi: UnsplashApi,
  private val searchPhotoErrorMapper: SearchPhotoErrorMapper,
  private val appCoroutineDispatchers: AppCoroutineDispatchers,
) : SearchPhotoRepository {
  override suspend fun search(query: String) =
    withContext(appCoroutineDispatchers.io) {
      Either
        .catch {
          unsplashApi
            .searchPhotos(query)
            .results
            .map(CoverPhotoResponse::toCoverPhoto)
        }.onLeft {
          Napier.e(
            throwable = it,
            tag = "RealSearchPhotoRepository",
            message = "search($query) failed",
          )
        }.mapLeft(searchPhotoErrorMapper)
    }
}

private fun CoverPhotoResponse.toCoverPhoto() =
  CoverPhoto(
    id = id,
    slug = slug,
    createdAt = createdAt,
    updatedAt = updatedAt,
    promotedAt = promotedAt,
    width = width,
    height = height,
    thumbnailUrl = urls.thumb,
  )
