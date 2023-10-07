package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components.ErrorMessageAndRetryButton
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_ui.components.LoadingIndicator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.compose_lifecycle.collectAsStateWithLifecycleKmp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.PhotoDetailRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.GetPhotoDetailByIdUseCase
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.components.CreatorInfoCard
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation.components.LargePhotoImage
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import org.koin.compose.koinInject

@Composable
private fun photoDetailViewModel(
  route: PhotoDetailRoute,
  getPhotoDetailByIdUseCase: GetPhotoDetailByIdUseCase = koinInject(),
): PhotoDetailViewModel =
  kmpViewModel(
    key = "${PhotoDetailViewModel::class.simpleName}_$route",
    factory =
      viewModelFactory {
        PhotoDetailViewModel(
          savedStateHandle = createSavedStateHandle(),
          getPhotoDetailByIdUseCase = getPhotoDetailByIdUseCase,
        )
      },
  )

@Composable
internal fun PhotoDetailScreen(
  route: PhotoDetailRoute,
  onNavigationBack: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: PhotoDetailViewModel = photoDetailViewModel(route = route),
) {
  val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycleKmp()

  val processIntent: (PhotoDetailViewIntent) -> Unit =
    remember(viewModel) {
      @Suppress("SuspiciousCallableReferenceInLambda")
      viewModel::process
    }

  DisposableEffect(processIntent) {
    processIntent(PhotoDetailViewIntent.Init)
    onDispose {}
  }

  PhotoDetailContent(
    modifier = modifier,
    route = route,
    uiState = uiState,
    onRetry = { processIntent(PhotoDetailViewIntent.Retry) },
    onNavigationBack = onNavigationBack,
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PhotoDetailContent(
  route: PhotoDetailRoute,
  uiState: PhotoDetailUiState,
  onRetry: () -> Unit,
  onNavigationBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = "Photo detail") },
        navigationIcon = {
          IconButton(onClick = onNavigationBack) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
      )
    },
  ) { padding ->
    Box(
      modifier =
        modifier
          .fillMaxSize()
          .padding(padding)
          .consumeWindowInsets(padding),
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
            errorMessage =
              when (uiState.error) {
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
            modifier =
              Modifier
                .matchParentSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
          ) {
            Spacer(modifier = Modifier.height(16.dp))

            CreatorInfoCard(
              modifier = Modifier.fillMaxWidth(),
              creator = detail.creator,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
              text = "Size: ${detail.size.width} x ${detail.size.height}",
              style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(4.dp))

            LargePhotoImage(
              modifier = Modifier.fillMaxWidth(),
              route = route,
              url = detail.fullUrl,
              contentDescription = detail.description,
              size = detail.size,
            )

            Spacer(modifier = Modifier.height(16.dp))
          }
        }
      }
    }
  }
}
