package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.InternalNavigationApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal.destinationId
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
      .storeFor(route.destinationId)
      .getOrCreate(T::class) { factory() }
  }
}
