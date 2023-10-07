package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.stable_wrappers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.jvm.JvmInline

@Stable
@JvmInline
value class StableWrapper<T>(
  val value: T,
) {
  operator fun component1(): T = value
}

@Immutable
@JvmInline
value class ImmutableWrapper<T>(
  val value: T,
) {
  operator fun component1(): T = value
}

@Suppress("NOTHING_TO_INLINE")
@Stable
inline fun <T> T.toImmutableWrapper(): ImmutableWrapper<T> = ImmutableWrapper(this)

@Suppress("NOTHING_TO_INLINE")
@Stable
inline fun <T> T.toStableWrapper(): StableWrapper<T> = StableWrapper(this)
