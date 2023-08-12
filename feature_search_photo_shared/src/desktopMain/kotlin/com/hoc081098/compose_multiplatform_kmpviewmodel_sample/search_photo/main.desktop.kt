package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Composable
fun SearchPhotoScreen(
  modifier: Modifier = Modifier,
) {
  LaunchedEffect(Unit) {
    Napier.base(DebugAntilog())
  }

  SearchPhotoScreenWithKoin(
    modifier = modifier
  )
}

actual fun isDebug(): Boolean = true