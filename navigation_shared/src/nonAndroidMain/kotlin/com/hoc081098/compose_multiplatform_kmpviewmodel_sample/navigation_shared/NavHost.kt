/**
 * Copied from [com.freeletics.khonshu.navigation.compose.NavHost.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/NavHost.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.InternalNavigationApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.MultiStackNavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.NavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.StackEntry
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.rememberNavigationExecutor
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.compose.ClearViewModelRegistry
import com.hoc081098.kmp.viewmodel.compose.ClearViewModelRegistryProvider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Create a new `NavHost` containing all given [destinations]. [startRoute] will be used as the
 * start destination of the graph. Use [com.freeletics.khonshu.navigation.NavEventNavigator] and
 * [NavigationSetup] to change what is shown in [NavHost].
 *
 * To support deep links a set of [DeepLinkHandlers][DeepLinkHandler] can be passed in optionally.
 * These will be used to build the correct back stack when the current `Activity` was launched with
 * an `ACTION_VIEW` `Intent` that contains an url in it's data. [deepLinkPrefixes] can be used to
 * provide a default set of url patterns that should be matched by any [DeepLinkHandler] that
 * doesn't provide its own [DeepLinkHandler.prefixes].
 *
 * The [destinationChangedCallback] can be used to be notified when the current destination
 * changes. Note that this will not be invoked when navigating to a [ActivityDestination].
 */
@OptIn(InternalNavigationApi::class)
@Composable
fun NavHost(
  startRoute: NavRoot,
  destinations: Set<NavDestination>,
  destinationChangedCallback: ((BaseRoute) -> Unit)? = null,
) {
  val executor = rememberNavigationExecutor(startRoute, destinations)

  DestinationChangedCallback(executor, destinationChangedCallback)

  val saveableStateHolder = rememberSaveableStateHolder()
  CompositionLocalProvider(LocalNavigationExecutor provides executor) {
    val entries = executor.visibleEntries.value
    Show(entries, executor, saveableStateHolder)
  }
}

@InternalNavigationApi
@Composable
private fun Show(
  entries: List<StackEntry<*>>,
  executor: MultiStackNavigationExecutor,
  saveableStateHolder: SaveableStateHolder,
) {
  entries.forEach { entry ->
    Show(entry, executor, saveableStateHolder)
  }
}

@InternalNavigationApi
@Composable
private fun <T : BaseRoute> Show(
  entry: StackEntry<T>,
  executor: MultiStackNavigationExecutor,
  saveableStateHolder: SaveableStateHolder,
) {
  // From AndroidX Navigation:
  //   Stash a reference to the SaveableStateHolder in the Store so that
  //   it is available when the destination is cleared. Which, because of animations,
  //   only happens after this leaves composition. Which means we can't rely on
  //   DisposableEffect to clean up this reference (as it'll be cleaned up too early)
  val clearViewModelRegistry = remember(entry, executor, saveableStateHolder) {
    executor
      .storeFor(entry.id)
      .getOrCreate(SaveableCloseable::class) {
        SaveableCloseable(entry.id.value, Ref(saveableStateHolder))
      }
      .clearViewModelRegistry
  }

  ClearViewModelRegistryProvider(clearViewModelRegistry) {
    saveableStateHolder.SaveableStateProvider(entry.id.value) {
      entry.destination.content(entry.route)
    }
  }
}

internal class Ref<T>(value: T) {
  private var value: T? = value

  fun get(): T? = value

  fun clear() {
    value = null
  }
}

internal class ClearViewModelRegistryCloseable(
  private val clearViewModelRegistryRef: ClearViewModelRegistry,
) : Closeable {
  override fun close() {
    clearViewModelRegistryRef.clear()
  }

  fun getClearViewModelRegistry(): ClearViewModelRegistry = clearViewModelRegistryRef
}

internal class SaveableCloseable(
  private val id: String,
  private val saveableStateHolderRef: Ref<SaveableStateHolder>,
) : Closeable {
  val clearViewModelRegistry = ClearViewModelRegistry()

  override fun close() {
    clearViewModelRegistry.clear()
    saveableStateHolderRef.get()?.removeState(id)
    saveableStateHolderRef.clear()
  }
}

@InternalNavigationApi
@Composable
private fun DestinationChangedCallback(
  executor: MultiStackNavigationExecutor,
  destinationChangedCallback: ((BaseRoute) -> Unit)?,
) {
  if (destinationChangedCallback != null) {
    LaunchedEffect(executor, destinationChangedCallback) {
      snapshotFlow { executor.visibleEntries.value }
        .map { it.last().route }
        .distinctUntilChanged()
        .collect { destinationChangedCallback(it) }
    }
  }
}

@InternalNavigationApi
val LocalNavigationExecutor: ProvidableCompositionLocal<NavigationExecutor> = staticCompositionLocalOf {
  throw IllegalStateException("Can't use NavEventNavigationHandler outside of a navigator NavHost")
}
