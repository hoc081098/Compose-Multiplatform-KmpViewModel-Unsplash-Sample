package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.EmptyView
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.ErrorMessageAndRetryButton
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.LoadingIndicator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi.collectAsStateWithLifecycle
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoError
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoUseCase
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoUiState.PhotoUiItem
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject

@Composable
private fun searchPhotoViewModel(
  searchPhotoUseCase: SearchPhotoUseCase = koinInject(),
): SearchPhotoViewModel =
  kmpViewModel(
    factory = {
      SearchPhotoViewModel(
        savedStateHandle = createSavedStateHandle(),
        searchPhotoUseCase = searchPhotoUseCase
      )
    },
  )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchPhotoScreen(
  navigateToPhotoDetail: (id: String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SearchPhotoViewModel = searchPhotoViewModel(),
) {
  val state by viewModel.stateFlow.collectAsStateWithLifecycle()
  val searchTerm by viewModel
    .searchTermStateFlow
    .collectAsStateWithLifecycle(context = viewModel.viewModelScope.coroutineContext)

  Column(
    modifier = modifier.fillMaxSize()
      .background(color = MaterialTheme.colorScheme.background),
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    TextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      value = searchTerm.orEmpty(),
      onValueChange = remember(viewModel) { viewModel::search },
      label = { Text(text = "Search term") },
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      text = "Submitted term: ${state.submittedTerm.orEmpty()}",
    )

    Spacer(modifier = Modifier.height(16.dp))

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentAlignment = Alignment.Center,
    ) {
      ListContent(
        modifier = Modifier.matchParentSize(),
        state = state,
        onItemClick = { navigateToPhotoDetail(it.id) }
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListContent(
  state: SearchPhotoUiState,
  onItemClick: (PhotoUiItem) -> Unit,
  modifier: Modifier = Modifier,
  lazyGridState: LazyGridState = rememberLazyGridState(),
) {
  if (state.isLoading) {
    LoadingIndicator(
      modifier = modifier,
    )
    return
  }

  state.error?.let { error ->
    ErrorMessageAndRetryButton(
      modifier = modifier,
      onRetry = { },
      errorMessage = when (error) {
        SearchPhotoError.NetworkError -> "Network error"
        SearchPhotoError.ServerError -> "Server error"
        SearchPhotoError.TimeoutError -> "Timeout error"
        SearchPhotoError.Unexpected -> "Unexpected error"
      },
    )
    return
  }

  if (state.photoUiItems.isEmpty()) {
    EmptyView(
      modifier = modifier,
    )
  } else {
    LazyVerticalGrid(
      modifier = modifier,
      columns = GridCells.Adaptive(minSize = 128.dp),
      state = lazyGridState,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(
        items = state.photoUiItems,
        key = { it.id },
      ) {
        PhotoGridCell(
          modifier = Modifier
            .animateItemPlacement()
            .fillMaxWidth()
            .aspectRatio(1f),
          photo = it,
          onClick = { onItemClick(it) },
        )
      }
    }
  }
}

@Composable
private fun PhotoGridCell(
  photo: PhotoUiItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clickable(onClick = onClick),
  ) {
    KamelImage(
      modifier = Modifier.matchParentSize(),
      resource = asyncPainterResource(data = photo.thumbnailUrl),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      animationSpec = tween(),
      onFailure = {
        Icon(
          modifier = Modifier.align(Alignment.Center),
          imageVector = Icons.Default.Info,
          contentDescription = null,
        )
      },
    )
  }
}