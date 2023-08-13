package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain

import arrow.core.Either

interface PhotoDetailRepository {
  suspend fun getPhotoDetailById(id: String): Either<PhotoDetailError, PhotoDetail>
}
