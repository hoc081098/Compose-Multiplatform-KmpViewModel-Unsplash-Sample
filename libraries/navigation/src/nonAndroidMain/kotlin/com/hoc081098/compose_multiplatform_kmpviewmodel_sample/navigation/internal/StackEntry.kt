/**
 * Copied from [com.freeletics.khonshu.navigation.compose.internal.StackEntry.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/StackEntry.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import androidx.compose.runtime.Immutable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ContentDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import kotlin.jvm.JvmInline

@InternalNavigationApi
@Immutable
internal data class StackEntry<T : BaseRoute>(
  val id: Id,
  val route: T,
  val destination: ContentDestination<T>,
) {
  val destinationId
    get() = route.destinationId

  val removable
    // cast is needed for the compiler to recognize that the when is exhaustive
    @Suppress("USELESS_CAST")
    get() =
      when (route as BaseRoute) {
        is NavRoute -> true
        is NavRoot -> false
        else -> throw IllegalStateException("Unknown route type: $route")
      }

  @JvmInline
  value class Id(
    val value: String,
  )
}
