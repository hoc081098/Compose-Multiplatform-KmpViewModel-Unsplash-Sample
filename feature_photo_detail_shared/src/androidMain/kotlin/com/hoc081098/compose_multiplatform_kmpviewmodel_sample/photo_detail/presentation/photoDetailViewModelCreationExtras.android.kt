package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.compose.defaultCreationExtras

@Composable
internal actual fun photoDetailViewModelCreationExtras(id: String): CreationExtras = defaultCreationExtras()
