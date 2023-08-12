package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalNativeApi

fun SearchPhotoViewController(
  navigateToPhotoDetail: (id: String) -> Unit = {},
): UIViewController = ComposeUIViewController {
  SearchPhotoScreenWithKoin(
    navigateToPhotoDetail = navigateToPhotoDetail
  )
}

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary