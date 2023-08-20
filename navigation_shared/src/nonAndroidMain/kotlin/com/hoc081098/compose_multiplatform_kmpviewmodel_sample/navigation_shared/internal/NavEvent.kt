package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.NavRoute

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
