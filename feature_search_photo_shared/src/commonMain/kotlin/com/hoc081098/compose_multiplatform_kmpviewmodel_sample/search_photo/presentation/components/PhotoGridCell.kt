package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components.LoadingIndicator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoUiState.PhotoUiItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun PhotoGridCell(
  photo: PhotoUiItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .clickable(onClick = onClick),
  ) {
    KamelImage(
      modifier = Modifier.matchParentSize(),
      resource = asyncPainterResource(data = photo.thumbnailUrl),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      animationSpec = tween(),
      onLoading = {
        LoadingIndicator(
          modifier = Modifier.matchParentSize(),
        )
      },
      onFailure = {
        Icon(
          modifier = Modifier.align(Alignment.Center),
          imageVector = Icons.Default.Info,
          contentDescription = null,
        )
      },
    )
  }
}
