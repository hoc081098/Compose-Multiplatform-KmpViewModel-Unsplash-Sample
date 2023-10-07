package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.rememberCloseableForRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState.PhotoSizeUi
import com.hoc081098.kmp.viewmodel.Closeable
import io.github.aakira.napier.Napier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.LocalKamelConfig
import kotlin.jvm.JvmField
import kotlin.math.roundToInt

private class KamelConfigWrapperCloseable(
  @JvmField val kamelConfig: KamelConfig,
) : Closeable {
  override fun close() {
    kamelConfig.runCatching { imageVectorCache.clear() }
    Napier.d(message = "Clear imageVectorCache")
  }
}

@Composable
internal fun LargePhotoImage(
  route: PhotoDetailRoute,
  url: String,
  contentDescription: String?,
  size: PhotoSizeUi,
  modifier: Modifier = Modifier,
) {
  val currentKamelConfig = LocalKamelConfig.current

  val kamelConfigWrapper =
    rememberCloseableForRoute(route) {
      KamelConfigWrapperCloseable(
        KamelConfig {
          takeFrom(currentKamelConfig)

          // Cache only 1 image
          imageBitmapCacheSize = 1

          // Disable cache
          imageVectorCacheSize = 0
          svgCacheSize = 0
        },
      )
    }

  CompositionLocalProvider(LocalKamelConfig provides kamelConfigWrapper.kamelConfig) {
    KamelImage(
      modifier =
        modifier
          .aspectRatio(size.width.toFloat() / size.height.toFloat())
          .clip(RoundedCornerShape(size = 8.dp)),
      resource = asyncPainterResource(data = url),
      contentDescription = contentDescription,
      contentScale = ContentScale.Crop,
      onLoading = {
        Column(
          modifier = Modifier.align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          CircularProgressIndicator(
            progress = it,
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "${(it * 100).roundToInt()} %",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
          )
        }
      },
      onFailure = {
        Icon(
          modifier = Modifier.align(Alignment.Center),
          imageVector = Icons.Default.Person,
          contentDescription = null,
        )
      },
    )
  }
}
