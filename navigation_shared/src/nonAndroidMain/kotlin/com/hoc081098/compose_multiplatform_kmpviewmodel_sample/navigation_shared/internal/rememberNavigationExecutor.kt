package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.ContentDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavRoot
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle

@InternalNavigationApi
@Composable
internal fun rememberNavigationExecutor(
  startRoot: NavRoot,
  destinations: Set<NavDestination>,
): MultiStackNavigationExecutor {
  val viewModel = kmpViewModel(
    factory = { StoreViewModel(globalSavedStateHandle = createSavedStateHandle()) },
  )

  return remember(viewModel) {
    val contentDestinations = destinations
      .filterIsInstance<ContentDestination<*>>()

    val stack = MultiStack.createWith(
      startRoot,
      contentDestinations,
      viewModel::removeEntry,
    )

    MultiStackNavigationExecutor(
      stack = stack,
      viewModel = viewModel,
    )
  }
}
