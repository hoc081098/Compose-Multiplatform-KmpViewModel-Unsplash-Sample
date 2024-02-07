package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailScreenRoute

@Composable
fun PhotoDetailScreen(
  route: PhotoDetailScreenRoute,
  modifier: Modifier = Modifier,
) {
  PhotoDetailScreenWithKoin(
    modifier = modifier,
    route = route,
  )
}

actual fun isDebug(): Boolean = true
