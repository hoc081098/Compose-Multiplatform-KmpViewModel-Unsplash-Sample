package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.BaseRoute
import com.freeletics.khonshu.navigation.NavEventNavigator
import com.freeletics.khonshu.navigation.compose.NavDestination
import com.freeletics.khonshu.navigation.compose.NavigationSetup
import com.freeletics.khonshu.navigation.compose.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.declareMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.intoMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import org.koin.compose.koinInject
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

@JvmField
val AllDestinationsQualifier = qualifier("AllDestinationsQualifier")

@JvmField
val NavigationModule = module {
  singleOf(::NavEventNavigator)

  declareMultibinding<BaseRoute, NavDestination>(qualifier = AllDestinationsQualifier)

  intoMultibinding(
    key = SearchPhotoRoute::class.java,
    multibindingQualifier = AllDestinationsQualifier,
  ) {
    ScreenDestination<SearchPhotoRoute> {
      NavigationSetup(koinInject())

      val navEventNavigator = koinInject<NavEventNavigator>()

      SearchPhotoScreen(
        navigateToPhotoDetail = remember(navEventNavigator) {
          {
            navEventNavigator.navigateTo(PhotoDetailRoute(id = it))
          }
        },
      )
    }
  }

  intoMultibinding(
    key = PhotoDetailRoute::class.java,
    multibindingQualifier = AllDestinationsQualifier,
  ) {
    ScreenDestination<PhotoDetailRoute> {
      NavigationSetup(koinInject())

      PhotoDetailScreen(
        route = it,
      )
    }
  }
}
