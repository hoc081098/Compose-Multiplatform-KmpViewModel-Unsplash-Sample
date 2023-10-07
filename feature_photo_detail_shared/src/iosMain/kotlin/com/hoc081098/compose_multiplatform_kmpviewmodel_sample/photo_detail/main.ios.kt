package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.ui.window.ComposeUIViewController
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import kotlin.experimental.ExperimentalNativeApi
import platform.UIKit.UIViewController

fun PhotoDetailViewController(route: PhotoDetailRoute): UIViewController =
  ComposeUIViewController {
    PhotoDetailScreenWithKoin(
      route = route,
    )
  }

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary
