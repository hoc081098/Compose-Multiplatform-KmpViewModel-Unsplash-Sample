package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.InternalNavigationApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.MultiStackNavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.NavEvent
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.NavigationExecutor
import kotlinx.coroutines.withContext

/**
 * Sets up the [NavEventNavigator] inside the current composition so that it's events
 * are handled while the composition is active.
 */
@OptIn(InternalNavigationApi::class)
@Composable
public fun NavigationSetup(navigator: NavEventNavigator) {
  val executor = LocalNavigationExecutor.current as MultiStackNavigationExecutor

  LaunchedEffect(executor, navigator) {
    withContext(executor.viewModel.viewModelScope.coroutineContext) {
      navigator
        .navEvents
        .collect(executor::navigate)
    }
  }
}

@InternalNavigationApi
private fun NavigationExecutor.navigate(
  event: NavEvent,
) {
  when (event) {
    is NavEvent.NavigateToEvent -> {
      navigate(event.route)
    }

    is NavEvent.NavigateToRootEvent -> {
      navigate(event.root, event.restoreRootState)
    }

    is NavEvent.UpEvent -> {
      navigateUp()
    }

    is NavEvent.BackEvent -> {
      navigateBack()
    }

    is NavEvent.BackToEvent -> {
      navigateBackTo(event.popUpTo, event.inclusive)
    }

    is NavEvent.ResetToRoot -> {
      resetToRoot(event.root)
    }

    is NavEvent.MultiNavEvent -> {
      event.navEvents.forEach { navigate(it) }
    }
  }
}