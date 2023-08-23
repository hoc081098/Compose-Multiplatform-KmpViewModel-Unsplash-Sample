package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal

/**
 * Platform agnostic _WeakReference_ wrapper.
 *
 * Basically just wrapping underlying platform implementations.
 */
internal expect class WeakReference<T : Any> constructor(referred: T) {
  fun get(): T?
  fun clear()
}

internal inline fun <T : Any> T.weaken(): WeakReference<T> = WeakReference(this)
