package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

fun main() {
    startKoin {
        logger(PrintLogger(level = Level.DEBUG))
    }

    Napier.base(DebugAntilog())

    application {

        var id by remember { mutableStateOf(null as String?) }

        Window(onCloseRequest = ::exitApplication) {
            SearchPhotoScreen(
                navigateToPhotoDetail = {
                    id = it
                }
            )
        }

        id?.let {
            Window(
                title = "Photo detail for $it",
                onCloseRequest =  { id = null }
            ) {
                Text(text = "Photo detail for $it")
            }
        }
    }
}