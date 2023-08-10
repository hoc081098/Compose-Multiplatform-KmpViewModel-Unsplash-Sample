package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data.response.SearchPhotosResult

internal interface UnsplashApi {
  suspend fun searchPhotos(query: String): SearchPhotosResult
}