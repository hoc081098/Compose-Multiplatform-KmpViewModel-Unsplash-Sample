package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.domain

interface SearchPhotoRepository {
  suspend fun search(query: String): List<CoverPhoto>
}