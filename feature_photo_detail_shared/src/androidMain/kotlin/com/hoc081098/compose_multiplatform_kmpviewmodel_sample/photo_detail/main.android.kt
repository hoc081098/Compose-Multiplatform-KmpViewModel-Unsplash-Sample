package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhotoDetailScreen(
  id: String,
  modifier: Modifier = Modifier,
) {
  PhotoDetailScreenWithKoin(
    modifier = modifier,
    id = id,
  )
}

actual fun isDebug(): Boolean = BuildConfig.DEBUG
