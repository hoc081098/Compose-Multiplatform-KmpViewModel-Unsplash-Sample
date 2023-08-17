package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import android.os.Parcelable

actual sealed interface BaseRoute : Parcelable
actual interface NavRoute : com.freeletics.khonshu.navigation.NavRoute, Parcelable, BaseRoute
actual interface NavRoot : com.freeletics.khonshu.navigation.NavRoot, Parcelable, BaseRoute
