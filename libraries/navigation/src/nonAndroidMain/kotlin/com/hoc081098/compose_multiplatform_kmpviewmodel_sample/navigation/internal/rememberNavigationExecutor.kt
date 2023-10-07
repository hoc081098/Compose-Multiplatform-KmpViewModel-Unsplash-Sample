/**
 * Copied from [com.freeletics.khonshu.navigation.compose.internal.MultiStackNavigationExecutorBuilder.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/MultiStackNavigationExecutorBuilder.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ContentDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory

@InternalNavigationApi
@Composable
internal fun rememberNavigationExecutor(
  startRoot: NavRoot,
  destinations: Set<NavDestination>,
): MultiStackNavigationExecutor {
  val viewModel =
    kmpViewModel(
      factory = viewModelFactory { StoreViewModel(globalSavedStateHandle = createSavedStateHandle()) },
    )

  return remember(viewModel) {
    val contentDestinations =
      destinations
        .filterIsInstance<ContentDestination<*>>()

    val stack =
      MultiStack.createWith(
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
