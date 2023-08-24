package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.NavEventNavigator
import com.freeletics.khonshu.navigation.compose.NavDestination
import com.freeletics.khonshu.navigation.compose.NavigationSetup
import com.freeletics.khonshu.navigation.compose.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.declareSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.intoSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import org.koin.compose.koinInject
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

@Stable
@JvmField
val AllDestinationsQualifier = qualifier("AllDestinationsQualifier")

@JvmField
val NavigationModule = module {
  singleOf(::NavEventNavigator)

  declareSetMultibinding<NavDestination>(qualifier = AllDestinationsQualifier)

  intoSetMultibinding(
    key = SearchPhotoRoute::class.java,
    multibindingQualifier = AllDestinationsQualifier,
  ) {
    ScreenDestination<SearchPhotoRoute> {
      val navEventNavigator = koinInject<NavEventNavigator>()

      NavigationSetup(navEventNavigator)

      SearchPhotoScreen(
        navigateToPhotoDetail = remember(navEventNavigator) {
          {
            navEventNavigator.navigateTo(PhotoDetailRoute(id = it))
          }
        },
      )
    }
  }

  intoSetMultibinding(
    key = PhotoDetailRoute::class.java,
    multibindingQualifier = AllDestinationsQualifier,
  ) {
    ScreenDestination<PhotoDetailRoute> {
      val navEventNavigator = koinInject<NavEventNavigator>()

      NavigationSetup(navEventNavigator)

      PhotoDetailScreen(
        route = it,
        onNavigationBack = remember(navEventNavigator) { navEventNavigator::navigateBack },
      )
    }
  }
}
