package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.compose_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.flow.StateFlow

@Immutable
enum class LifecycleState {
  CREATED,
  STARTED,
  RESUMED,
}

@Composable
expect inline fun <T> StateFlow<T>.collectAsStateWithLifecycleKmp(
  minActiveState: LifecycleState = LifecycleState.STARTED,
  context: CoroutineContext = EmptyCoroutineContext,
): State<T>
