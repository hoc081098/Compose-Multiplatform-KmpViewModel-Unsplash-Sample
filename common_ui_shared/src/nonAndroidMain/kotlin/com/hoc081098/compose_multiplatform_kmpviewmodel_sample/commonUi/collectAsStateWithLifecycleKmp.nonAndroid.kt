package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.commonUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycleKmp(
  minActiveState: LifecycleState,
  context: CoroutineContext,
): State<T> = collectAsState(context = context)
