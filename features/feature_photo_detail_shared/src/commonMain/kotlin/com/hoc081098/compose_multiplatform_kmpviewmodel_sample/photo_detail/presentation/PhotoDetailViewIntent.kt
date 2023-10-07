package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface PhotoDetailViewIntent {
  data object Init : PhotoDetailViewIntent
  data object Retry : PhotoDetailViewIntent
  data object Refresh : PhotoDetailViewIntent
}
