/**
 * Copied from [com.freeletics.khonshu.navigation.internal.NavEvent.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/internal/NavEvent.kt)
 */
package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute

@InternalNavigationApi
public sealed interface NavEvent {
  @InternalNavigationApi
  public data class NavigateToEvent(
    internal val route: NavRoute,
  ) : NavEvent

  @InternalNavigationApi
  public data class NavigateToRootEvent(
    internal val root: NavRoot,
    internal val restoreRootState: Boolean,
  ) : NavEvent

  @InternalNavigationApi
  public data object UpEvent : NavEvent

  @InternalNavigationApi
  public data object BackEvent : NavEvent

  @InternalNavigationApi
  public data class BackToEvent(
    internal val popUpTo: DestinationId<*>,
    internal val inclusive: Boolean,
  ) : NavEvent

  @InternalNavigationApi
  public data class ResetToRoot(
    internal val root: NavRoot,
  ) : NavEvent

  @InternalNavigationApi
  public data class MultiNavEvent(
    internal val navEvents: List<NavEvent>,
  ) : NavEvent
}
