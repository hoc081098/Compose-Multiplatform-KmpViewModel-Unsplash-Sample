package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.response.CoverPhotoResponse

internal interface UnsplashApi {
  suspend fun getPhotoDetailById(id: String): CoverPhotoResponse
}
