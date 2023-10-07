package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

/**
 * Platform agnostic _WeakReference_ wrapper.
 *
 * Basically just wrapping underlying platform implementations.
 */
internal expect class WeakReference<T : Any>(
  referred: T,
) {
  public fun get(): T?

  public fun clear()
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T : Any> T.weaken(): WeakReference<T> = WeakReference(this)
