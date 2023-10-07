package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils.rememberKoinModulesForRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.SearchPhotoRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.domainModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoScreen
import io.github.aakira.napier.Napier
import kotlin.jvm.JvmField
import org.koin.dsl.module

@JvmField
internal val FeatureSearchPhotoModule =
  module {
    includes(
      dataModule(),
      domainModule(),
    )
  }

@Composable
internal fun SearchPhotoScreenWithKoin(
  route: SearchPhotoRoute,
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val loaded by rememberKoinModulesForRoute(
    route = route,
    unloadModules = true,
  ) { listOf(FeatureSearchPhotoModule) }

  if (loaded) {
    SearchPhotoScreen(
      modifier = modifier,
      navigateToPhotoDetail = navigateToPhotoDetail,
    )
  } else {
    SideEffect {
      Napier.d(
        message = "SearchPhotoScreenWithKoin: unloaded",
        tag = "SearchPhotoScreenWithKoin",
      )
    }
  }
}

@Immutable
enum class BuildFlavor {
  DEV,
  PROD,
  ;

  companion object {
    @Stable
    val Current: BuildFlavor by lazy {
      when (BuildKonfig.FLAVOR) {
        "dev" -> DEV
        "prod" -> PROD
        else -> error("Unknown flavor ${BuildKonfig.FLAVOR}")
      }
    }
  }
}

expect fun isDebug(): Boolean
