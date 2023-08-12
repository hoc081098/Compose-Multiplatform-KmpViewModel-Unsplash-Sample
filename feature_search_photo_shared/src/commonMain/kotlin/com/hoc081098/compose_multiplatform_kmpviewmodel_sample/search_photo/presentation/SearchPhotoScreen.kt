package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoUseCase
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import org.koin.compose.koinInject

@Composable
internal fun SearchPhotoScreen(
  modifier: Modifier = Modifier,
  searchPhotoUseCase: SearchPhotoUseCase = koinInject(),
  viewModel: SearchPhotoViewModel = kmpViewModel(
    factory = {
      SearchPhotoViewModel(
        savedStateHandle = createSavedStateHandle(),
        searchPhotoUseCase = searchPhotoUseCase
      )
    },
  ),
) {
  Text("SearchPhotoScreen $viewModel")
}