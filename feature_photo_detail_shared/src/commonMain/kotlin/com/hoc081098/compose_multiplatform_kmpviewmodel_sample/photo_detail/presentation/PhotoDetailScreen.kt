package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.ErrorMessageAndRetryButton
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.LoadingIndicator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.collectAsStateWithLifecycleKmp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.GetPhotoDetailByIdUseCase
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.components.CreatorInfoCard
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import org.koin.compose.koinInject

@Suppress("NOTHING_TO_INLINE")
internal expect inline fun photoDetailViewModelCreationExtras(id: String): CreationExtras

@Composable
private fun photoDetailViewModel(
  id: String,
  getPhotoDetailByIdUseCase: GetPhotoDetailByIdUseCase = koinInject(),
): PhotoDetailViewModel = kmpViewModel(
  key = "${PhotoDetailViewModel::class.simpleName}_$id",
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
  val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycleKmp()

  val processIntent: (PhotoDetailViewIntent) -> Unit = remember(viewModel) {
    @Suppress("SuspiciousCallableReferenceInLambda")
    viewModel::process
  }

  DisposableEffect(processIntent) {
    processIntent(PhotoDetailViewIntent.Init)
    onDispose {}
  }

  PhotoDetailContent(
    modifier = modifier,
    uiState = uiState,
    onRetry = { processIntent(PhotoDetailViewIntent.Retry) },
  )
}

@Composable
private fun PhotoDetailContent(
  uiState: PhotoDetailUiState,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.background),
  ) {
    when (uiState) {
      PhotoDetailUiState.Loading -> {
        LoadingIndicator(
          modifier = Modifier.matchParentSize(),
        )
      }

      is PhotoDetailUiState.Error -> {
        ErrorMessageAndRetryButton(
          modifier = Modifier.matchParentSize(),
          onRetry = onRetry,
          errorMessage = when (uiState.error) {
            PhotoDetailError.NetworkError -> "Network error"
            PhotoDetailError.ServerError -> "Server error"
            PhotoDetailError.TimeoutError -> "Timeout error"
            PhotoDetailError.Unexpected -> "Unexpected error"
          },
        )
      }

      is PhotoDetailUiState.Content -> {
        val detail = uiState.photoDetail

        Column(
          modifier = Modifier
            .matchParentSize()
            .verticalScroll(rememberScrollState()),
        ) {
          Spacer(modifier = Modifier.height(16.dp))

          CreatorInfoCard(
            modifier = Modifier.fillMaxWidth(),
            creator = detail.creator,
          )
        }
      }
    }
  }
}
