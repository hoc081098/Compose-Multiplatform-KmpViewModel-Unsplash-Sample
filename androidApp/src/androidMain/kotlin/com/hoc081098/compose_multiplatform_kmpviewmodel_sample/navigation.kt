package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.NavEventNavigator
import com.freeletics.khonshu.navigation.compose.NavDestination
import com.freeletics.khonshu.navigation.compose.NavigationSetup
import com.freeletics.khonshu.navigation.compose.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.declareSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.intoSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.SearchPhotoRoute
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
    ScreenDestination<SearchPhotoRoute> { route ->
      val navEventNavigator = koinInject<NavEventNavigator>()

      NavigationSetup(navEventNavigator)

      SearchPhotoScreen(
        route = route,
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
    ScreenDestination<PhotoDetailRoute> { route ->
      val navEventNavigator = koinInject<NavEventNavigator>()

      NavigationSetup(navEventNavigator)

      PhotoDetailScreen(
        route = route,
        onNavigationBack = remember(navEventNavigator) { navEventNavigator::navigateBack },
      )
    }
  }
}
