package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import com.freeletics.khonshu.navigation.internal.InternalNavigationApi

@OptIn(InternalNavigationApi::class)
actual val EXTRA_ROUTE: String
  get() =
    com
      .freeletics
      .khonshu
      .navigation
      .EXTRA_ROUTE
