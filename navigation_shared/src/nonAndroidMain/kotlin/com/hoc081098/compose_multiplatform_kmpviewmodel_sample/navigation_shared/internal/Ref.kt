package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared.internal

internal expect class WeakReference<T : Any> constructor(referred: T) {
  fun get(): T?
  fun clear()
}

internal inline fun <T : Any> T.weakReference(): WeakReference<T> = WeakReference(this)
