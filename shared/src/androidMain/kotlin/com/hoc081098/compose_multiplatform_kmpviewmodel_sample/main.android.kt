package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Composable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.App

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()
