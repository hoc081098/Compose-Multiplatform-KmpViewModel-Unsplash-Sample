/**
 * Copied from [com.freeletics.khonshu.navigation.NavRoute](https://github.com/freeletics/khonshu/blob/b14ca695f36f5f7165f7c8f435f487f356f53cd6/navigation/src/commonMain/kotlin/com/freeletics/khonshu/navigation/NavRoute.kt#L5)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

@Immutable
expect sealed interface BaseRoute : Parcelable

/**
 * Represents the route to a destination.
 *
 * The instance of this will be put into the navigation arguments as a [Parcelable] and is then
 * available to the target screens.
 */
@Immutable
expect interface NavRoute :
  Parcelable,
  BaseRoute

/**
 * This is similar to a [NavRoute] but represents the route to the start destination used in
 * a backstack. When you navigate to a [NavRoot] the current backstack is saved and removed
 * so that the [NavRoot] is right on top of the start destination.
 *
 * The instance of this will be put into the navigation arguments as a [Parcelable] and is then
 * available to the target screens.
 */
@Immutable
expect interface NavRoot :
  Parcelable,
  BaseRoute
