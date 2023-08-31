package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.compose_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.StateFlow

@Composable
actual inline fun <T> StateFlow<T>.collectAsStateWithLifecycleKmp(
  minActiveState: LifecycleState,
  context: CoroutineContext,
): State<T> = collectAsState(context = context)
