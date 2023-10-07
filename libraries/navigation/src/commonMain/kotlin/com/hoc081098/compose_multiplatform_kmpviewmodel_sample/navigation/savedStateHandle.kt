package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import com.hoc081098.kmp.viewmodel.SavedStateHandle

inline fun <T : BaseRoute> SavedStateHandle.requireRoute(): T =
  requireNotNull(get<T>(EXTRA_ROUTE)) {
    "SavedStateHandle doesn't contain Route data in \"$EXTRA_ROUTE\""
  }

fun SavedStateHandle.putArguments(baseRoute: BaseRoute) {
  this[EXTRA_ROUTE] = baseRoute
}
