package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import com.hoc081098.kmp.viewmodel.SavedStateHandle

inline fun <T : NavRoute> SavedStateHandle.requireNavRoute(): T = requireNotNull(get<T>(EXTRA_ROUTE)) {
  "SavedStateHandle doesn't contain NavRoute data in \"$EXTRA_ROUTE\""
}

fun SavedStateHandle.putArguments(baseRoute: NavRoute) {
  this[EXTRA_ROUTE] = baseRoute
}

inline fun <T : NavRoot> SavedStateHandle.requireNavRoot(): T = requireNotNull(get<T>(EXTRA_ROUTE)) {
  "SavedStateHandle doesn't contain NavRoot data in \"$EXTRA_ROUTE\""
}

fun SavedStateHandle.putArguments(baseRoute: NavRoot) {
  this[EXTRA_ROUTE] = baseRoute
}
