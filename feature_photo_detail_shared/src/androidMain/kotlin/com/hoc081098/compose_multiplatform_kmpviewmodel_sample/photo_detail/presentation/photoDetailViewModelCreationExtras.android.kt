package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Stable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.compose.defaultPlatformCreationExtras

@Suppress("NOTHING_TO_INLINE")
@Stable
internal actual inline fun photoDetailViewModelCreationExtras(route: PhotoDetailRoute): CreationExtras =
  defaultPlatformCreationExtras() // Handled by Freeletics Khonshu Navigation
