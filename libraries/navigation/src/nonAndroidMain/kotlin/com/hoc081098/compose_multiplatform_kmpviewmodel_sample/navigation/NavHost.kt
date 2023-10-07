/**
 * Copied from [com.freeletics.khonshu.navigation.compose.NavHost.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/NavHost.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.InternalNavigationApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.MultiStackNavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.NavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.StackEntry
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.WeakReference
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.rememberNavigationExecutor
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.weaken
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.SavedStateHandleFactoryProvider
import com.hoc081098.kmp.viewmodel.compose.ViewModelStoreOwnerProvider
import kotlin.jvm.JvmField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Create a new `NavHost` containing all given [destinations]. [startRoute] will be used as the
 * start destination of the graph. Use [NavEventNavigator] and
 * [NavigationSetup] to change what is shown in [NavHost].
 *
 * The [destinationChangedCallback] can be used to be notified when the current destination
 * changes.
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
  entries: ImmutableList<StackEntry<*>>,
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
  val viewModelStoreOwner =
    remember(entry, executor, saveableStateHolder) {
      executor
        .storeFor(entry.id)
        .getOrCreate(SaveableCloseable::class) {
          SaveableCloseable(
            id = entry.id.value,
            saveableStateHolderRef = saveableStateHolder.weaken(),
          )
        }.viewModelStoreOwnerState
    }.value ?: run {
      println("----------------------------------- START NAVIGATION [1] -----------------------------------")
      println("visibleEntries=${executor.visibleEntries.value.joinToString(separator = "\n") { " --> $it" }}")
      println("entry=$entry")
      println("viewModelStoreOwner is null")
      println("----------------------------------- END NAVIGATION [1] -----------------------------------")
      return
    }

  SideEffect {
    println("----------------------------------- START NAVIGATION [2] -----------------------------------")
    println("visibleEntries=${executor.visibleEntries.value.joinToString(separator = "\n") { " --> $it" }}")
    println("entry=$entry")
    println("viewModelStoreOwner=$viewModelStoreOwner")
    println("----------------------------------- END NAVIGATION [2] -----------------------------------")
  }

  val savedStateHandleFactory =
    remember(entry, executor) {
      SavedStateHandleFactory {
        executor
          .storeFor(entry.id)
          .getOrCreate(SavedStateHandle::class) {
            SavedStateHandle().apply { putArguments(entry.route) }
          }
      }
    }

  SavedStateHandleFactoryProvider(savedStateHandleFactory) {
    ViewModelStoreOwnerProvider(viewModelStoreOwner) {
      saveableStateHolder.SaveableStateProvider(entry.id.value) {
        entry.destination.content(entry.route)
      }
    }
  }
}

internal class DefaultViewModelStoreOwner : ViewModelStoreOwner {
  private val viewModelStoreLazy = lazy(LazyThreadSafetyMode.NONE) { ViewModelStore() }
  override val viewModelStore: ViewModelStore by viewModelStoreLazy

  fun clearIfInitialized() {
    if (viewModelStoreLazy.isInitialized()) {
      viewModelStore.clear()
    }
  }
}

internal class SaveableCloseable(
  @JvmField val id: String,
  @JvmField val saveableStateHolderRef: WeakReference<SaveableStateHolder>,
) : Closeable {
  private val viewModelStoreOwnerRef: MutableState<DefaultViewModelStoreOwner?> =
    mutableStateOf(DefaultViewModelStoreOwner())

  val viewModelStoreOwnerState: State<DefaultViewModelStoreOwner?> = viewModelStoreOwnerRef

  override fun close() {
    println("----------------------------------- START NAVIGATION [3] -----------------------------------")

    viewModelStoreOwnerRef.value?.clearIfInitialized()
    println("cleared viewModelStoreOwner")
    viewModelStoreOwnerRef.value = null
    println("set viewModelStoreOwner to null")

    saveableStateHolderRef.get()?.removeState(id)
    println("removed state $id from saveableStateHolder")
    saveableStateHolderRef.clear()
    println("cleared saveableStateHolderRef")

    println("----------------------------------- END NAVIGATION [3] -----------------------------------")
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
val LocalNavigationExecutor: ProvidableCompositionLocal<NavigationExecutor> =
  staticCompositionLocalOf {
    throw IllegalStateException("Can't use NavEventNavigationHandler outside of a navigator NavHost")
  }
