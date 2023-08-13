package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.GetPhotoDetailByIdUseCase
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import org.koin.compose.koinInject

@Composable
internal expect fun photoDetailViewModelCreationExtras(id: String): CreationExtras

@Composable
private fun photoDetailViewModel(
  id: String,
  getPhotoDetailByIdUseCase: GetPhotoDetailByIdUseCase = koinInject(),
): PhotoDetailViewModel = kmpViewModel(
  key = "PhotoDetailViewModel_$id",
  factory = {
    PhotoDetailViewModel(
      savedStateHandle = createSavedStateHandle(),
      getPhotoDetailByIdUseCase = getPhotoDetailByIdUseCase,
    )
  },
  extras = photoDetailViewModelCreationExtras(id = id),
)

@Composable
internal fun PhotoDetailScreen(
  id: String,
  modifier: Modifier = Modifier,
  viewModel: PhotoDetailViewModel = photoDetailViewModel(id = id),
) {
}
