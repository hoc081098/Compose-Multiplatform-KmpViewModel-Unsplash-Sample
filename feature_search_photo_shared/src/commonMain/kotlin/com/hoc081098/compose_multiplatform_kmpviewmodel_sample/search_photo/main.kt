package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared.rememberKoinModules
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.domainModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoScreen
import com.hoc081098.kmp.viewmodel.compose.ClearViewModelRegistry
import kotlin.jvm.JvmField
import org.koin.dsl.module

@JvmField
internal val FeatureSearchPhotoModule = module {
  includes(
    dataModule(),
    domainModule(),
  )
}

@Composable
internal fun SearchPhotoScreenWithKoin(
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
  clearViewModelRegistry: ClearViewModelRegistry? = null,
) {
  val loaded = rememberKoinModules(unloadModules = true) { listOf(FeatureSearchPhotoModule) }

  if (loaded) {
    MaterialTheme {
      SearchPhotoScreen(
        modifier = modifier,
        clearViewModelRegistry = clearViewModelRegistry,
        navigateToPhotoDetail = navigateToPhotoDetail,
      )
    }
  }
}

enum class BuildFlavor {
  DEV,
  PROD,
  ;

  companion object {
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
