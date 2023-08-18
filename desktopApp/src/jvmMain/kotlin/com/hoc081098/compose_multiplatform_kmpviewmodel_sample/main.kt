package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import com.hoc081098.kmp.viewmodel.compose.ClearViewModelRegistry
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
    val currentDestination by remember { derivedStateOf { stack.last() } }
    val clearViewModelRegistries = remember {
      mutableMapOf<BaseRoute, ClearViewModelRegistry>()
    }

    LaunchedEffect(Unit) {
      snapshotFlow { stack.toList() }.collect {
        println(
          "[StoreViewModel] clearViewModelRegistries: ${
            clearViewModelRegistries.mapValues {(_, v) ->
              "${v::class.simpleName}@${v.hashCode()}"
            }
          }",
        )
        println("[StoreViewModel] stack: ${it.toList()}")
      }
    }

    val stateHolder = rememberSaveableStateHolder()

    Window(onCloseRequest = ::exitApplication) {
      stateHolder.SaveableStateProvider(currentDestination) {
        when (val route = currentDestination) {
          SearchPhotoRoute -> {
            SearchPhotoScreen(
              clearViewModelRegistry = clearViewModelRegistries.getOrPut(route) { ClearViewModelRegistry() },
              navigateToPhotoDetail = {
                stack += PhotoDetailRoute(it)
              },
            )
          }

          is PhotoDetailRoute -> {
            PhotoDetailScreen(
              route = route,
              clearViewModelRegistry = clearViewModelRegistries.getOrPut(route) { ClearViewModelRegistry() },
              onNavigationBack = {
                val last = stack.removeLastOrNull()
                last?.let {
                  stateHolder.removeState(it)
                  clearViewModelRegistries.remove(it)?.clear()
                }
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
