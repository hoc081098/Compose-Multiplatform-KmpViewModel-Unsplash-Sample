package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

import arrow.core.Either

interface SearchPhotoRepository {
  suspend fun search(query: String): Either<SearchPhotoError, List<CoverPhoto>>
}
