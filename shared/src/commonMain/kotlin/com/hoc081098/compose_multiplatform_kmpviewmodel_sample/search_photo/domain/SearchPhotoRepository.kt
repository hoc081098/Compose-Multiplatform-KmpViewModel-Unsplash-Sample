package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

interface SearchPhotoRepository {
  suspend fun search(query: String): List<CoverPhoto>
}