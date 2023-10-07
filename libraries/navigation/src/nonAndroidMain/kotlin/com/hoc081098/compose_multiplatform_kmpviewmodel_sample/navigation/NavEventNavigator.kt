/**
 * Copied from [com.freeletics.khonshu.navigation.NavEventNavigator.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/NavEventNavigator.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.DestinationId
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.InternalNavigationApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.NavEvent
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal.NavEventCollector
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This allows to trigger navigation actions from outside the view layer
 * without keeping references to Android framework classes that might leak. It also improves
 * the testability of your navigation logic since it is possible to just write test that
 * the correct events were emitted.
 */
@OptIn(InternalNavigationApi::class)
public open class NavEventNavigator {
  private val _navEvents = Channel<NavEvent>(Channel.UNLIMITED)

  public val navEvents: Flow<NavEvent> =
    flow {
      for (result in _navEvents) {
        emit(result)
      }
    }

  /**
   * Triggers a new [NavEvent] to navigate to the given [route].
   */
  public fun navigateTo(route: NavRoute) {
    val event = NavEvent.NavigateToEvent(route)
    sendNavEvent(event)
  }

  /**
   * Triggers a new [NavEvent] to navigate to the given [root]. The current back stack will
   * be popped and saved. Whether the backstack of the given `root` is restored depends on
   * [restoreRootState].
   */
  public fun navigateToRoot(
    root: NavRoot,
    restoreRootState: Boolean = false,
  ) {
    val event = NavEvent.NavigateToRootEvent(root, restoreRootState)
    sendNavEvent(event)
  }

  /**
   * Triggers a new [NavEvent] that causes up navigation.
   */
  public fun navigateUp() {
    val event = NavEvent.UpEvent
    sendNavEvent(event)
  }

  /**
   * Triggers a new [NavEvent] that pops the back stack to the previous destination.
   */
  public fun navigateBack() {
    val event = NavEvent.BackEvent
    sendNavEvent(event)
  }

  /**
   * Triggers a new [NavEvent] that collects and combines the nav events sent in the block so they can be
   * handled individually.
   *
   * Note: This should be used when navigating multiple times, for example calling `navigateBackTo`
   * followed by `navigateTo`.
   */
  public fun navigate(block: NavEventCollector.() -> Unit) {
    val navEvents = NavEventCollector().apply(block).navEvents
    sendNavEvent(NavEvent.MultiNavEvent(navEvents))
  }

  /**
   * Triggers a new [NavEvent] that pops the back stack to [T]. If [inclusive] is
   * `true` [T] itself will also be popped.
   */
  public inline fun <reified T : NavRoute> navigateBackTo(inclusive: Boolean = false) {
    navigateBackTo(DestinationId(T::class), inclusive)
  }

  @PublishedApi
  internal fun <T : BaseRoute> navigateBackTo(
    popUpTo: DestinationId<T>,
    inclusive: Boolean = false,
  ) {
    val event = NavEvent.BackToEvent(popUpTo, inclusive)
    sendNavEvent(event)
  }

  /**
   * Reset the back stack to the given [root]. The current back stack will cleared and if
   * root was already on it it will be recreated.
   */
  public fun resetToRoot(root: NavRoot) {
    val event = NavEvent.ResetToRoot(root)
    sendNavEvent(event)
  }

  private fun sendNavEvent(event: NavEvent) {
    val result = _navEvents.trySendBlocking(event)
    check(result.isSuccess)
  }
}
