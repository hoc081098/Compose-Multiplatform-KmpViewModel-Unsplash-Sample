package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.domain

class SearchPhotoUseCase(
  private val searchPhotoRepository: SearchPhotoRepository,
) {
  suspend operator fun invoke(query: String): List<CoverPhoto> =
    searchPhotoRepository.search(query)
}