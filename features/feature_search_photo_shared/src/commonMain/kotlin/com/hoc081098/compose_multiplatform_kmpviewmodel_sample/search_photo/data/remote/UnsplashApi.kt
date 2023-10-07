package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote

internal interface UnsplashApi {
  suspend fun searchPhotos(query: String): SearchPhotosResult
}
