package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.compose.LocalNavigationExecutor
import com.freeletics.khonshu.navigation.internal.InternalNavigationApi
import com.freeletics.khonshu.navigation.internal.destinationId
import com.hoc081098.kmp.viewmodel.Closeable

@OptIn(InternalNavigationApi::class)
@Composable
actual inline fun <reified T : Closeable> rememberCloseable(
  route: BaseRoute,
  crossinline factory: @DisallowComposableCalls () -> T,
): T {
  val executor = LocalNavigationExecutor.current

  return remember(executor, route) {
    executor
      .storeFor(route.asKhonshuNavRoute().destinationId)
      .getOrCreate(T::class) { factory() }
  }
}

@PublishedApi
@Suppress("NOTHING_TO_INLINE")
internal inline fun BaseRoute.asKhonshuNavRoute(): com.freeletics.khonshu.navigation.BaseRoute = when (this) {
  is NavRoot -> this
  is NavRoute -> this
  else -> throw IllegalStateException("Unknown route type: $this")
}
