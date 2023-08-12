package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

fun main() = application {
    startKoin {
        logger(PrintLogger(level = Level.DEBUG))
    }

    Window(onCloseRequest = ::exitApplication) {
        SearchPhotoScreen()
    }
}