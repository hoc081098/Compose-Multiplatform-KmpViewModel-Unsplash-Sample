/**
 * Copied from [com.freeletics.khonshu.navigation.internal.MultiStackNavigationExecutor.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/MultiStackNavigationExecutor.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import androidx.compose.runtime.State
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import kotlin.jvm.JvmField
import kotlinx.collections.immutable.ImmutableList

@InternalNavigationApi
internal class MultiStackNavigationExecutor(
  private val stack: MultiStack,
  @JvmField internal val viewModel: StoreViewModel,
) : NavigationExecutor {
  @Suppress("unused") // TODO
  val visibleEntries: State<ImmutableList<StackEntry<*>>>
    get() = stack.visibleEntries

  @Suppress("unused") // TODO
  val canNavigateBack: State<Boolean>
    get() = stack.canNavigateBack

  override fun navigate(route: NavRoute) {
    stack.push(route)
  }

  override fun navigate(
    root: NavRoot,
    restoreRootState: Boolean,
  ) {
    stack.push(root, clearTargetStack = !restoreRootState)
  }

  override fun navigateUp() {
    stack.popCurrentStack()
  }

  override fun navigateBack() {
    stack.pop()
  }

  override fun <T : BaseRoute> navigateBackTo(
    destinationId: DestinationId<T>,
    isInclusive: Boolean,
  ) {
    stack.popUpTo(destinationId, isInclusive)
  }

  override fun resetToRoot(root: NavRoot) {
    stack.resetToRoot(root)
  }

  override fun <T : BaseRoute> routeFor(destinationId: DestinationId<T>): T = entryFor(destinationId).route

  override fun <T : BaseRoute> savedStateHandleFor(destinationId: DestinationId<T>): SavedStateHandle {
    val entry = entryFor(destinationId)
    return viewModel.provideSavedStateHandle(entry.id)
  }

  override fun <T : BaseRoute> storeFor(destinationId: DestinationId<T>): NavigationExecutor.Store {
    val entry = entryFor(destinationId)
    return storeFor(entry.id)
  }

  override fun <T : BaseRoute> extra(destinationId: DestinationId<T>): Any {
    val entry = entryFor(destinationId)
    return entry.destination.extra!!
  }

  internal fun storeFor(entryId: StackEntry.Id): NavigationExecutor.Store = viewModel.provideStore(entryId)

  private fun <T : BaseRoute> entryFor(destinationId: DestinationId<T>): StackEntry<T> =
    stack.entryFor(destinationId)
      ?: throw IllegalStateException("Route $destinationId not found on back stack")
}
