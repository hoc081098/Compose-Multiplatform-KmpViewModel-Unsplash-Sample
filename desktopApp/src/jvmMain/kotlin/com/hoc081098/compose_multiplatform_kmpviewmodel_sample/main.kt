package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MainView()
    }
}