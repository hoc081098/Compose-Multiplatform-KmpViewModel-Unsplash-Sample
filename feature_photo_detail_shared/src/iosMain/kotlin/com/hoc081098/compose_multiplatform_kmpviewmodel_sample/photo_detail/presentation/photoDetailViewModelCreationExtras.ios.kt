package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.compose.defaultCreationExtras
import com.hoc081098.kmp.viewmodel.edit

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun photoDetailViewModelCreationExtras(id: String): CreationExtras =
  defaultCreationExtras().edit {
    this[SAVED_STATE_HANDLE_KEY] = SavedStateHandle(
      mapOf(
        PhotoDetailViewModel.ID_KEY to id,
      ),
    )
  }
