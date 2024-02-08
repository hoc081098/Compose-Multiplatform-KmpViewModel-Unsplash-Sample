package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.theme.AppTheme
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils.koinInjectSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavHost
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableSet
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      KoinAndroidContext {
        AppTheme {
          NavHost(
            startRoute = SearchPhotoScreenRoute,
            destinations = koinInjectSetMultibinding<NavDestination>(AllDestinationsQualifier)
              .let { remember(it) { it.toImmutableSet() } },
            navEventNavigator = koinInject(),
            destinationChangedCallback = { route ->
              Napier.d(message = "Destination changed: $route", tag = "MainActivity")
            },
          )
        }
      }
    }
  }
}
