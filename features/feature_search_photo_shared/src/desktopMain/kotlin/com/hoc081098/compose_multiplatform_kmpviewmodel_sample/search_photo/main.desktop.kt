package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute

@NonRestartableComposable
@Composable
fun SearchPhotoScreen(
  route: SearchPhotoScreenRoute,
  modifier: Modifier = Modifier,
) = SearchPhotoScreenWithKoin(
  modifier = modifier,
  route = route,
)

actual fun isDebug(): Boolean = true
