package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

import arrow.core.Either
import org.koin.core.annotation.Factory

@Factory
class SearchPhotoUseCase(
  private val searchPhotoRepository: SearchPhotoRepository,
) {
  suspend operator fun invoke(query: String): Either<SearchPhotoError, List<CoverPhoto>> = searchPhotoRepository.search(query)
}
