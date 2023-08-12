package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.response.SearchPhotosResult

internal interface UnsplashApi {
  suspend fun searchPhotos(query: String): SearchPhotosResult
}