package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable

@Immutable
actual sealed interface BaseRoute : Parcelable

@Immutable
actual interface NavRoute :
  com.freeletics.khonshu.navigation.NavRoute,
  Parcelable,
  BaseRoute

@Immutable
actual interface NavRoot :
  com.freeletics.khonshu.navigation.NavRoot,
  Parcelable,
  BaseRoute

@Suppress("NOTHING_TO_INLINE")
public inline fun BaseRoute.asKhonshuBaseRoute(): com.freeletics.khonshu.navigation.BaseRoute =
  when (this) {
    is NavRoot -> this
    is NavRoute -> this
    else -> throw IllegalStateException("Unknown route type: $this")
  }
