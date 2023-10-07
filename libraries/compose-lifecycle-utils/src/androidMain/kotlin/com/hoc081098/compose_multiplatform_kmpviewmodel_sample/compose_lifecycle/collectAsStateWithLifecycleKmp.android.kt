package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.compose_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle as androidXCollectAsStateWithLifecycle
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.StateFlow

@Suppress("NOTHING_TO_INLINE")
@Composable
actual inline fun <T> StateFlow<T>.collectAsStateWithLifecycleKmp(
  minActiveState: LifecycleState,
  context: CoroutineContext,
): State<T> =
  androidXCollectAsStateWithLifecycle(
    minActiveState = minActiveState.toAndroidXLifecycleState(),
    context = context,
  )

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun LifecycleState.toAndroidXLifecycleState(): Lifecycle.State =
  when (this) {
    LifecycleState.CREATED -> Lifecycle.State.CREATED
    LifecycleState.STARTED -> Lifecycle.State.STARTED
    LifecycleState.RESUMED -> Lifecycle.State.RESUMED
  }
