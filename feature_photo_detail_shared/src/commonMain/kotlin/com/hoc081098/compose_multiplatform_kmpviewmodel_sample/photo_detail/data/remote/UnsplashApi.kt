package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.response.CoverPhotoResponse

internal interface UnsplashApi {
  suspend fun getPhotoDetailById(id: String): CoverPhotoResponse
}
