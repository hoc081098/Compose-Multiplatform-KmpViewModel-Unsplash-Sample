package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import org.koin.core.logger.Level as KoinLoggerLevel
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.CommonModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.theme.AppTheme
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils.koinInjectSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute
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

  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "KmpViewModel Compose Multiplatform",
    ) {
      KoinApplication(application = koinApplication) {
        AppTheme {
          NavHost(
            startRoute = SearchPhotoScreenRoute,
            destinations = koinInjectSetMultibinding<NavDestination>(AllDestinationsQualifier)
              .toImmutableSet(),
            navEventNavigator = koinInject(),
            destinationChangedCallback = { println("Destination changed: $it") },
          )
        }
      }
    }
  }
}
