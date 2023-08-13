package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.BuildKonfig
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.domainModule
import kotlin.jvm.JvmField
import org.koin.dsl.module

@JvmField
internal val FeaturePhotoDetailModule = module {
  includes(
    dataModule(),
    domainModule(),
  )
}

@Composable
internal fun SearchPhotoScreenWithKoin(
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
) {
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
