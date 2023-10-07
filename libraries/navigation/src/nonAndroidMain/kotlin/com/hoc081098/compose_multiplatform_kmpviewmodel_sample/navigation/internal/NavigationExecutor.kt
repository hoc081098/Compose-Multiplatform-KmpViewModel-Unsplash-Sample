/**
 * Copied from [com.freeletics.khonshu.navigation.internal.NavigationExecutor.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/internal/NavigationExecutor.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import kotlin.reflect.KClass

@InternalNavigationApi
public interface NavigationExecutor {
  public fun navigate(route: NavRoute)

  public fun navigate(
    root: NavRoot,
    restoreRootState: Boolean,
  )

  public fun navigateUp()

  public fun navigateBack()

  public fun <T : BaseRoute> navigateBackTo(
    destinationId: DestinationId<T>,
    isInclusive: Boolean,
  )

  public fun resetToRoot(root: NavRoot)

  public fun <T : BaseRoute> routeFor(destinationId: DestinationId<T>): T

  public fun <T : BaseRoute> savedStateHandleFor(destinationId: DestinationId<T>): SavedStateHandle

  public fun <T : BaseRoute> storeFor(destinationId: DestinationId<T>): Store

  public fun <T : BaseRoute> extra(destinationId: DestinationId<T>): Any

  @InternalNavigationApi
  public interface Store {
    public fun <T : Any> getOrCreate(
      key: KClass<T>,
      factory: () -> T,
    ): T
  }
}
