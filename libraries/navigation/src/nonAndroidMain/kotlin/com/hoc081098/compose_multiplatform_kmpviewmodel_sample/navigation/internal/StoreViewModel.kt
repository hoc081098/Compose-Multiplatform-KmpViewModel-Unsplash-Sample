/**
 * Copied from [com.freeletics.khonshu.navigation.compose.internal.StoreViewModel.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/StoreViewModel.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel

@InternalNavigationApi
internal class StoreViewModel(
  internal val globalSavedStateHandle: SavedStateHandle,
) : ViewModel() {
  init {
    println("🔥 StoreViewModel init")
  }

  private val stores = mutableMapOf<StackEntry.Id, NavigationExecutorStore>()
  private val savedStateHandles = mutableMapOf<StackEntry.Id, SavedStateHandle>()

  fun provideStore(id: StackEntry.Id): NavigationExecutor.Store = stores.getOrPut(id) { NavigationExecutorStore() }

  fun provideSavedStateHandle(id: StackEntry.Id): SavedStateHandle = savedStateHandles.getOrPut(id) { SavedStateHandle() }

  fun removeEntry(id: StackEntry.Id) {
    val store = stores.remove(id)
    store?.close()

    savedStateHandles.remove(id)
    globalSavedStateHandle.remove<Any>(id.value)
  }

  public override fun onCleared() {
    for (store in stores.values) {
      store.close()
    }
    stores.clear()

    for (key in savedStateHandles.keys) {
      globalSavedStateHandle.remove<Any>(key.value)
    }
    savedStateHandles.clear()
  }
}
