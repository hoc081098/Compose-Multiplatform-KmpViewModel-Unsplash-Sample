package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle as androidXCollectAsStateWithLifecycle

@Composable
@Suppress("StateFlowValueCalledInComposition")
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
  minActiveState: LifecycleState,
  context: CoroutineContext,
): State<T> = androidXCollectAsStateWithLifecycle(
  minActiveState = minActiveState.toAndroidXLifecycleState(),
  context = context,
)

@Suppress("NOTHING_TO_INLINE")
private inline fun LifecycleState.toAndroidXLifecycleState(): Lifecycle.State =
  when (this) {
    LifecycleState.DESTROYED -> Lifecycle.State.DESTROYED
    LifecycleState.INITIALIZED -> Lifecycle.State.INITIALIZED
    LifecycleState.CREATED -> Lifecycle.State.CREATED
    LifecycleState.STARTED -> Lifecycle.State.STARTED
    LifecycleState.RESUMED -> Lifecycle.State.RESUMED
  }
