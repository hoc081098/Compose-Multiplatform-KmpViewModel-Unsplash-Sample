package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.freeletics.khonshu.navigation.NavEventNavigator
import com.freeletics.khonshu.navigation.compose.NavDestination
import com.freeletics.khonshu.navigation.compose.NavigationSetup
import com.freeletics.khonshu.navigation.compose.ScreenDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.declareSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.intoSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailScreenRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute
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
val NavigationModule =
  module {
    singleOf(::NavEventNavigator)

    declareSetMultibinding<NavDestination>(qualifier = AllDestinationsQualifier)

    intoSetMultibinding(
      key = SearchPhotoScreenRoute::class.java,
      multibindingQualifier = AllDestinationsQualifier,
    ) {
      ScreenDestination<SearchPhotoScreenRoute> { route ->
        val navigator = koinInject<NavEventNavigator>()

        NavigationSetup(navigator)

        SearchPhotoScreen(
          route = route,
          navigateToPhotoDetail =
            remember(navigator) {
              {
                navigator.navigateTo(PhotoDetailScreenRoute(id = it))
              }
            },
        )
      }
    }

    intoSetMultibinding(
      key = PhotoDetailScreenRoute::class.java,
      multibindingQualifier = AllDestinationsQualifier,
    ) {
      ScreenDestination<PhotoDetailScreenRoute> { route ->
        val navigator = koinInject<NavEventNavigator>()

        NavigationSetup(navigator)

        PhotoDetailScreen(
          route = route,
          onNavigationBack = remember(navigator) { navigator::navigateBack },
        )
      }
    }
  }
