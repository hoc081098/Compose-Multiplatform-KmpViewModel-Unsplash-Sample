package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

actual sealed interface BaseRoute : Parcelable
actual interface NavRoute : BaseRoute, Parcelable
actual interface NavRoot : BaseRoute, Parcelable
