package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.ui.window.ComposeUIViewController
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.App

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }