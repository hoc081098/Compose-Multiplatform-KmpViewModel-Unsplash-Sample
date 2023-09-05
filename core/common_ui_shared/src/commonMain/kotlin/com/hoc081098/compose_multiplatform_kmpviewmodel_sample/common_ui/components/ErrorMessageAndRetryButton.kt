package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessageAndRetryButton(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
  retryText: String = "Retry",
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(text = errorMessage)

      Spacer(modifier = Modifier.height(16.dp))

      Button(onClick = onRetry) {
        Text(text = retryText)
      }
    }
  }
}
