/**
 * Copied from [com.freeletics.khonshu.navigation.internal.DestinationId.kt](https://github.com/freeletics/khonshu/blob/1e7732b44e1abf04e2e7468c99300dc140132da1/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/internal/DestinationId.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

@InternalNavigationApi
@JvmInline
public value class DestinationId<T : BaseRoute>(
  public val route: KClass<T>,
)

@InternalNavigationApi
public val <T : BaseRoute> T.destinationId: DestinationId<out T>
  get() = DestinationId(this::class)
