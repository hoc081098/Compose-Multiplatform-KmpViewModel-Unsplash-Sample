package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components.LoadingIndicator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.PhotoDetailUiState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
internal fun CreatorInfoCard(
  creator: PhotoDetailUiState.PhotoCreatorUi,
  modifier: Modifier = Modifier,
) {
  ElevatedCard(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
  ) {
    Row(
      modifier =
        Modifier
          .fillMaxWidth()
          .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (creator.mediumProfileImageUrl != null) {
        KamelImage(
          modifier =
            Modifier
              .size(65.dp)
              .clip(CircleShape),
          resource = asyncPainterResource(data = creator.mediumProfileImageUrl),
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
              imageVector = Icons.Default.Person,
              contentDescription = null,
            )
          },
        )
      } else {
        Box(
          modifier = Modifier.size(65.dp),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Default.Person,
            contentDescription = null,
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Top,
      ) {
        Text(
          modifier =
            Modifier
              .fillMaxWidth()
              .basicMarquee(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          text = creator.name,
          style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          horizontalArrangement = Arrangement.Start,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            painter = painterResource("ic_unsplash_svg.xml"),
            contentDescription = null,
          )

          Text(
            text = "@${creator.username}",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
    }
  }
}
