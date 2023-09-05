package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyView(
  modifier: Modifier = Modifier,
  text: String = "Empty",
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    Text(text = text)
  }
}
