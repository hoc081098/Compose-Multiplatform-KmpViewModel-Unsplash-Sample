package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute

@Composable
fun PhotoDetailScreen(
  route: PhotoDetailRoute,
  onNavigationBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  PhotoDetailScreenWithKoin(
    modifier = modifier,
    route = route,
    onNavigationBack = onNavigationBack,
  )
}

actual fun isDebug(): Boolean = BuildConfig.DEBUG
