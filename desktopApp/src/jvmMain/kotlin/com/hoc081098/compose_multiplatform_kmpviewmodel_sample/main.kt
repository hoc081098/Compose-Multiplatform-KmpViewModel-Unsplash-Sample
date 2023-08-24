package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavEventNavigator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavHost
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavigationSetup
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.Level
import java.util.logging.SimpleFormatter
import java.util.logging.StreamHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level as KoinLoggerLevel
import org.koin.core.logger.PrintLogger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun main() {
  val initJob = CoroutineScope(Dispatchers.IO).launch {
    Napier.base(
      DebugAntilog(
        handler = listOf(
          StreamHandler(System.out, SimpleFormatter()).apply {
            level = Level.ALL
          },
        ),
      ),
    )

    startKoin {
      logger(PrintLogger(level = KoinLoggerLevel.DEBUG))
      modules(
        module {
          singleOf(::NavEventNavigator)
        },
      )
    }
  }

  application {
    val isInitializationComplete by produceState(initialValue = false, initJob) {
      initJob.join()
      value = true
    }

    Window(
      onCloseRequest = ::exitApplication,
      title = "KmpViewModel Compose Multiplatform",
    ) {
      if (!isInitializationComplete) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "Loading...",
            modifier = Modifier,
          )
        }
        return@Window
      }

      NavHost(
        startRoute = SearchPhotoRoute,
        destinations = setOf(
          ScreenDestination<SearchPhotoRoute> {
            NavigationSetup(koinInject())

            val navEventNavigator = koinInject<NavEventNavigator>()

            SearchPhotoScreen(
              navigateToPhotoDetail = remember(navEventNavigator) {
                {
                  navEventNavigator.navigateTo(PhotoDetailRoute(id = it))
                }
              },
            )
          },
          ScreenDestination<PhotoDetailRoute> {
            val navigator = koinInject<NavEventNavigator>()
            NavigationSetup(navigator)

            PhotoDetailScreen(
              route = it,
              onNavigationBack = { navigator.navigateBack() },
            )
          },
        ),
      )
    }
  }
}
