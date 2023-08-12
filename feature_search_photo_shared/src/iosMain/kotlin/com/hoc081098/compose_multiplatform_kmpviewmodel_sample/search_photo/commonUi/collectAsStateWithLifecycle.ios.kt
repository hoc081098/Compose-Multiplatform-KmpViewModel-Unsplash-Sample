package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.commonUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
@Suppress("StateFlowValueCalledInComposition")
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
  minActiveState: LifecycleState,
  context: CoroutineContext,
): State<T> = collectAsState(context = context)

