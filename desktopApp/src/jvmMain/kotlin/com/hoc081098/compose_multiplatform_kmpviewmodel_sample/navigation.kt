package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.declareSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.intoSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavEventNavigator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavigationSetup
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ScreenDestination
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
val NavigationModule =
  module {
    singleOf(::NavEventNavigator)

    declareSetMultibinding<NavDestination>(qualifier = AllDestinationsQualifier)

    intoSetMultibinding(
      key = SearchPhotoRoute::class.java,
      multibindingQualifier = AllDestinationsQualifier,
    ) {
      ScreenDestination<SearchPhotoRoute> { route ->
        val navigator = koinInject<NavEventNavigator>()

        NavigationSetup(navigator)

        SearchPhotoScreen(
          route = route,
          navigateToPhotoDetail =
            remember(navigator) {
              {
                navigator.navigateTo(PhotoDetailRoute(id = it))
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
        val navigator = koinInject<NavEventNavigator>()

        NavigationSetup(navigator)

        PhotoDetailScreen(
          route = route,
          onNavigationBack = remember(navigator) { navigator::navigateBack },
        )
      }
    }
  }
