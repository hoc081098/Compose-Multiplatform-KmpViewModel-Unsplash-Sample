package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SearchPhotoError {
  data object NetworkError : SearchPhotoError
  data object TimeoutError : SearchPhotoError
  data object ServerError : SearchPhotoError
  data object Unexpected : SearchPhotoError
}
