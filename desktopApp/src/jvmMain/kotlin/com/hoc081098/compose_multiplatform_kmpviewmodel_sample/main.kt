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
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.theme.AppTheme
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.coroutines_utils.AppCoroutineDispatchers
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavEventNavigator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavHost
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavigationSetup
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.SearchPhotoRoute
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
          singleOf(::AppCoroutineDispatchers)
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
      AppTheme {
        if (!isInitializationComplete) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.White),
            contentAlignment = Alignment.Center,
          ) {
            Text(text = "Loading...")
          }
          return@AppTheme
        }

        NavHost(
          startRoute = SearchPhotoRoute,
          destinations = setOf(
            ScreenDestination<SearchPhotoRoute> { route ->
              val navigator = koinInject<NavEventNavigator>()

              NavigationSetup(navigator)

              SearchPhotoScreen(
                route = route,
                navigateToPhotoDetail = remember(navigator) {
                  {
                      id ->
                    navigator.navigateTo(PhotoDetailRoute(id = id))
                  }
                },
              )
            },
            ScreenDestination<PhotoDetailRoute> { route ->
              val navigator = koinInject<NavEventNavigator>()

              NavigationSetup(navigator)

              PhotoDetailScreen(
                route = route,
                onNavigationBack = remember(navigator) { navigator::navigateBack },
              )
            },
          ),
          destinationChangedCallback = remember {
            {
              println("Destination changed: $it")
            }
          },
        )
      }
    }
  }
}
