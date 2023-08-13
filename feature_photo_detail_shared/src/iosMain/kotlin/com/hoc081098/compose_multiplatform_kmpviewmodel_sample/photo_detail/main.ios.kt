package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.ui.window.ComposeUIViewController
import kotlin.experimental.ExperimentalNativeApi
import platform.UIKit.UIViewController

fun PhotoDetailViewController(
  id: String,
): UIViewController = ComposeUIViewController {
  PhotoDetailScreenWithKoin(
    id = id,
  )
}

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary
