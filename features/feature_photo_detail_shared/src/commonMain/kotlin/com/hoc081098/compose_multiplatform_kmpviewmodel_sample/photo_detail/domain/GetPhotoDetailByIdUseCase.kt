package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain

import arrow.core.Either
import org.koin.core.annotation.Factory

@Factory
class GetPhotoDetailByIdUseCase(
  private val repository: PhotoDetailRepository,
) {
  suspend operator fun invoke(id: String): Either<PhotoDetailError, PhotoDetail> = repository.getPhotoDetailById(id)
}
