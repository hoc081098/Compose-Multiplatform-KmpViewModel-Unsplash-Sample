package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.buildCreationExtras

@Composable
internal actual fun photoDetailViewModelCreationExtras(id: String): CreationExtras =
  buildCreationExtras {
    this[SAVED_STATE_HANDLE_KEY] = SavedStateHandle(
      mapOf(
        PhotoDetailViewModel.ID_KEY to id,
      ),
    )
  }
