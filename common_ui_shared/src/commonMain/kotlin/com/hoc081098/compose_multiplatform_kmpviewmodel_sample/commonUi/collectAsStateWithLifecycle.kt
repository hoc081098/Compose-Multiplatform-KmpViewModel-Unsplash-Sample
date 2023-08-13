package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.flow.StateFlow

enum class LifecycleState {
  DESTROYED,
  INITIALIZED,
  CREATED,
  STARTED,
  RESUMED,
}

@Composable
expect fun <T> StateFlow<T>.collectAsStateWithLifecycle(
  minActiveState: LifecycleState = LifecycleState.STARTED,
  context: CoroutineContext = EmptyCoroutineContext,
): State<T>
