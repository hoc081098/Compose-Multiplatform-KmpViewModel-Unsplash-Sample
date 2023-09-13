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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlin.math.roundToInt

@Composable
fun LargePhotoImage(
  url: String,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  KamelImage(
    modifier = modifier
      .aspectRatio(1f / 0.5f)
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
