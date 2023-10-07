/**
 * Copied from [com.freeletics.khonshu.navigation.internal.NavEventCollector.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/internal/NavEventCollector.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute

@OptIn(InternalNavigationApi::class)
public class NavEventCollector internal constructor() {
  private val _navEvents = mutableListOf<NavEvent>()
  internal val navEvents: List<NavEvent> = _navEvents

  public fun navigateTo(route: NavRoute) {
    val event = NavEvent.NavigateToEvent(route)
    _navEvents.add(event)
  }

  public fun navigateToRoot(
    root: NavRoot,
    restoreRootState: Boolean,
  ) {
    val event = NavEvent.NavigateToRootEvent(root, restoreRootState)
    _navEvents.add(event)
  }

  public fun navigateUp() {
    val event = NavEvent.UpEvent
    _navEvents.add(event)
  }

  public fun navigateBack() {
    val event = NavEvent.BackEvent
    _navEvents.add(event)
  }

  public inline fun <reified T : NavRoute> navigateBackTo(inclusive: Boolean) {
    navigateBackTo(DestinationId(T::class), inclusive)
  }

  @PublishedApi
  internal fun <T : NavRoute> navigateBackTo(
    destination: DestinationId<T>,
    inclusive: Boolean,
  ) {
    val event = NavEvent.BackToEvent(destination, inclusive)
    _navEvents.add(event)
  }

  public fun resetToRoot(root: NavRoot) {
    val event = NavEvent.ResetToRoot(root)
    _navEvents.add(event)
  }
}
