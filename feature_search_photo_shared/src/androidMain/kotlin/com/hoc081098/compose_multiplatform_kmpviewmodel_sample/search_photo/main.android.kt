package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchPhotoScreen(
  modifier: Modifier = Modifier,
) {
  SearchPhotoScreenWithKoin(
    modifier = modifier
  )
}


actual fun isDebug(): Boolean = BuildConfig.DEBUG