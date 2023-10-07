/**
 * Copied from [com.freeletics.khonshu.navigation.internal.NavigationExecutorStore](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation/src/androidMain/kotlin/com/freeletics/khonshu/navigation/internal/NavigationExecutorStore.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.kmp.viewmodel.Closeable
import kotlin.reflect.KClass

@InternalNavigationApi
public class NavigationExecutorStore :
  NavigationExecutor.Store,
  Closeable {
  private val storedObjects = mutableMapOf<KClass<*>, Any>()

  override fun <T : Any> getOrCreate(
    key: KClass<T>,
    factory: () -> T,
  ): T {
    @Suppress("UNCHECKED_CAST")
    var storedObject = storedObjects[key] as T?
    if (storedObject == null) {
      storedObject = factory()
      storedObjects[key] = storedObject
    }
    return storedObject
  }

  override fun close() {
    storedObjects.forEach { (_, storedObject) ->
      if (storedObject is Closeable) {
        storedObject.close()
      }
    }
    storedObjects.clear()
  }
}
