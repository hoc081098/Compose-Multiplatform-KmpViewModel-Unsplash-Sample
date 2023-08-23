package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.BaseRoute
import com.freeletics.khonshu.navigation.compose.NavDestination
import com.freeletics.khonshu.navigation.compose.NavHost
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.koinInjectMapMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import io.github.aakira.napier.Napier

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      NavHost(
        startRoute = SearchPhotoRoute,
        destinations = koinInjectMapMultibinding<BaseRoute, NavDestination>(AllDestinationsQualifier)
          .let { multibinding ->
            remember(multibinding) { multibinding.values.toSet() }
          },
        destinationChangedCallback = remember {
          {
              route ->
            Napier.d(
              message = "Destination changed: $route",
              tag = "MainActivity",
            )
          }
        },
      )
    }
  }
}
