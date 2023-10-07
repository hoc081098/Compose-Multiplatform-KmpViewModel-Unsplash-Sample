package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

@Immutable
actual sealed interface BaseRoute : Parcelable

@Immutable
actual interface NavRoute :
  BaseRoute,
  Parcelable

@Immutable
actual interface NavRoot :
  BaseRoute,
  Parcelable
