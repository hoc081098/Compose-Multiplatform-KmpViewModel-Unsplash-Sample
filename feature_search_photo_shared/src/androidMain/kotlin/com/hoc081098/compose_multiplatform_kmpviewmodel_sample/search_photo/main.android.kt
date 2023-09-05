package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute

@Composable
fun SearchPhotoScreen(
  route: SearchPhotoRoute,
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  SearchPhotoScreenWithKoin(
    route = route,
    modifier = modifier,
    navigateToPhotoDetail = navigateToPhotoDetail,
  )
}

actual fun isDebug(): Boolean = BuildConfig.DEBUG
