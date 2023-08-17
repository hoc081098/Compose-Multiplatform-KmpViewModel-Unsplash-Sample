package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.NavEventNavigator
import com.freeletics.khonshu.navigation.NavRoot
import com.freeletics.khonshu.navigation.NavRoute
import com.freeletics.khonshu.navigation.compose.NavigationSetup
import com.freeletics.khonshu.navigation.compose.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import kotlinx.parcelize.Parcelize
import org.koin.compose.koinInject
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

@Parcelize
data object SearchPhotoRoute : NavRoute, NavRoot

@Parcelize
data class PhotoDetailRoute(val id: String) : NavRoute

@JvmField
val AllDestinationsQualifier = qualifier("AllDestinationsQualifier")

@JvmField
val NavigationModule = module {
  singleOf(::NavEventNavigator)

  single(AllDestinationsQualifier) {
    setOf(
      ScreenDestination<SearchPhotoRoute> {
        NavigationSetup(koinInject())

        val navEventNavigator = koinInject<NavEventNavigator>()

        SearchPhotoScreen(
          navigateToPhotoDetail = remember(navEventNavigator) {
            {
              navEventNavigator.navigateTo(PhotoDetailRoute(id = it))
            }
          }
        )
      },
      ScreenDestination<PhotoDetailRoute> {
        NavigationSetup(koinInject())

        PhotoDetailScreen(
          id = it.id,
        )
      },
    )
  }
}
