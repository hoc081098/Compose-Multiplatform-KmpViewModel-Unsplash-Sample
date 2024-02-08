package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import androidx.compose.runtime.Stable
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.declareSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.intoSetMultibinding
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailScreenRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoScreenRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.PhotoDetailScreen
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.SearchPhotoScreen
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.navigation.ScreenDestination
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
      ScreenDestination<SearchPhotoScreenRoute> {
        SearchPhotoScreen(route = it)
      }
    }

    intoSetMultibinding(
      key = PhotoDetailScreenRoute::class.java,
      multibindingQualifier = AllDestinationsQualifier,
    ) {
      ScreenDestination<PhotoDetailScreenRoute> {
        PhotoDetailScreen(route = it)
      }
    }
  }
