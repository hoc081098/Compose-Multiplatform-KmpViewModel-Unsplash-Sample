package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

enum class LifecycleState {
  DESTROYED,
  INITIALIZED,
  CREATED,
  STARTED,
  RESUMED;
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
public expect fun <T> StateFlow<T>.collectAsStateWithLifecycle(
  minActiveState: LifecycleState = LifecycleState.STARTED,
  context: CoroutineContext = EmptyCoroutineContext,
): State<T>