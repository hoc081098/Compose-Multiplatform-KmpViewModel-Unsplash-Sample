package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PhotoDetailError {
  data object NetworkError : PhotoDetailError
  data object TimeoutError : PhotoDetailError
  data object ServerError : PhotoDetailError
  data object Unexpected : PhotoDetailError
}
