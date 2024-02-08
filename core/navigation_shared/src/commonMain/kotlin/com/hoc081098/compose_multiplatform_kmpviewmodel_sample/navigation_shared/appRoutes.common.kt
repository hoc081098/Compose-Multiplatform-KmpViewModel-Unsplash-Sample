package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavRoot
import com.hoc081098.solivagant.navigation.NavRoute

@Immutable
@Parcelize
data object SearchPhotoScreenRoute : NavRoot

@Immutable
@Parcelize
data class PhotoDetailScreenRoute(
  val id: String,
) : NavRoute
