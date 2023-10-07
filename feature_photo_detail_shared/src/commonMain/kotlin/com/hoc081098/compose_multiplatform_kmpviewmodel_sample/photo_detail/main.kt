package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils.rememberKoinModulesForRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.domainModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailScreen
import io.github.aakira.napier.Napier
import kotlin.jvm.JvmField
import org.koin.dsl.module

@JvmField
internal val FeaturePhotoDetailModule =
  module {
    includes(
      dataModule(),
      domainModule(),
    )
  }

@Composable
internal fun PhotoDetailScreenWithKoin(
  route: PhotoDetailRoute,
  onNavigationBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val loaded by rememberKoinModulesForRoute(
    route = route,
    unloadModules = true,
  ) { listOf(FeaturePhotoDetailModule) }

  if (loaded) {
    PhotoDetailScreen(
      modifier = modifier,
      route = route,
      onNavigationBack = onNavigationBack,
    )
  } else {
    SideEffect {
      Napier.d(
        message = "PhotoDetailScreenWithKoin: unloaded",
        tag = "PhotoDetailScreenWithKoin",
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
