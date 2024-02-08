package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import org.koin.core.logger.Level as KoinLoggerLevel
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.CommonModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.theme.AppTheme
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils.koinInjectSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute
import com.hoc081098.solivagant.lifecycle.LifecycleOwnerProvider
import com.hoc081098.solivagant.lifecycle.LifecycleRegistry
import com.hoc081098.solivagant.lifecycle.rememberLifecycleOwner
import com.hoc081098.solivagant.navigation.LifecycleControllerEffect
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavHost
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.Level
import java.util.logging.SimpleFormatter
import java.util.logging.StreamHandler
import kotlinx.collections.immutable.toImmutableSet
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.logger.PrintLogger
import org.koin.dsl.KoinAppDeclaration

fun main() {
  Napier.base(
    DebugAntilog(
      handler = listOf(
        StreamHandler(System.out, SimpleFormatter())
          .apply { level = Level.ALL },
      ),
    ),
  )

  val koinApplication: KoinAppDeclaration = {
    logger(PrintLogger(level = KoinLoggerLevel.DEBUG))

    modules(
      CommonModule,
      NavigationModule,
    )
  }

  val lifecycleRegistry = LifecycleRegistry()

  application {
    val windowState = rememberWindowState()

    LifecycleControllerEffect(
      lifecycleRegistry = lifecycleRegistry,
      windowState = windowState,
    )

    Window(
      onCloseRequest = ::exitApplication,
      title = "KmpViewModel Compose Multiplatform",
      state = windowState,
    ) {
      LifecycleOwnerProvider(
        lifecycleOwner = rememberLifecycleOwner(lifecycleRegistry),
      ) {
        KoinApplication(application = koinApplication) {
          AppTheme {
            NavHost(
              startRoute = SearchPhotoScreenRoute,
              destinations = koinInjectSetMultibinding<NavDestination>(AllDestinationsQualifier)
                .let { remember(it) { it.toImmutableSet() } },
              navEventNavigator = koinInject(),
              destinationChangedCallback = { route ->
                Napier.d(message = "Destination changed: $route", tag = "main")
              },
            )
          }
        }
      }
    }
  }
}
