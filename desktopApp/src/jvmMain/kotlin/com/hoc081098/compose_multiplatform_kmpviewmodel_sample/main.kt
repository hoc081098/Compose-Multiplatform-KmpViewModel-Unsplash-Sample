package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.Level
import java.util.logging.SimpleFormatter
import java.util.logging.StreamHandler
import org.koin.core.context.startKoin
import org.koin.core.logger.Level as KoinLoggerLevel
import org.koin.core.logger.PrintLogger

fun main() {
  startKoin {
    logger(PrintLogger(level = KoinLoggerLevel.DEBUG))
  }

  Napier.base(
    DebugAntilog(
      handler = listOf(
        StreamHandler(System.out, SimpleFormatter()).apply {
          level = Level.ALL
        },
      ),
    ),
  )

  application {
    val stack = remember { mutableStateListOf<BaseRoute>(SearchPhotoRoute) }
    val currentDestinationState = remember { derivedStateOf { stack.last() } }
    val stateHolder = rememberSaveableStateHolder()

    Window(onCloseRequest = ::exitApplication) {
      val route = currentDestinationState.value

      stateHolder.SaveableStateProvider(route) {
        when (route) {
          SearchPhotoRoute -> {
            SearchPhotoScreen(
              navigateToPhotoDetail = {
                stack += PhotoDetailRoute(it)
              },
            )
          }
          is PhotoDetailRoute -> {
            PhotoDetailScreen(
              route = route,
              onNavigationBack = {
                val last = stack.removeLastOrNull()
                last?.let { stateHolder.removeState(it) }
              },
            )
          }

          else -> {
            error("Unknown route $route")
          }
        }
      }
    }
  }
}
