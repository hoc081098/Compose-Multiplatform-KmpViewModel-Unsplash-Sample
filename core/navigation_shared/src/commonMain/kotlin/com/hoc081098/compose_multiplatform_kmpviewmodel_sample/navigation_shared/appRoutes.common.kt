package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Immutable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize

@Immutable
@Parcelize
data object SearchPhotoRoute : NavRoute, NavRoot

@Immutable
@Parcelize
data class PhotoDetailRoute(
  val id: String,
) : NavRoute
