package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.kmp.viewmodel.compose.ClearViewModelRegistry

@Composable
fun SearchPhotoScreen(
  clearViewModelRegistry: ClearViewModelRegistry,
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  SearchPhotoScreenWithKoin(
    modifier = modifier,
    navigateToPhotoDetail = navigateToPhotoDetail,
    clearViewModelRegistry = clearViewModelRegistry,
  )
}

actual fun isDebug(): Boolean = true
