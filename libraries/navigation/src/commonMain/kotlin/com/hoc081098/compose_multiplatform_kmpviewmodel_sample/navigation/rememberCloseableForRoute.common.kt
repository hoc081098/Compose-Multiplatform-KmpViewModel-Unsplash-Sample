package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import com.hoc081098.kmp.viewmodel.Closeable

@Composable
expect inline fun <reified T : Closeable> rememberCloseableForRoute(
  route: BaseRoute,
  crossinline factory: @DisallowComposableCalls () -> T,
): T
