package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.App

actual fun getPlatformName(): String = "Desktop"

@Composable fun MainView() = App()

@Preview
@Composable
fun AppPreview() {
    App()
}