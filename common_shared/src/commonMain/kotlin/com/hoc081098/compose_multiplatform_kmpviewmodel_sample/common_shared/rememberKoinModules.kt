package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.koin.compose.getKoin
import org.koin.core.Koin
import org.koin.core.module.Module

/**
 * Load and remember Modules & run CompositionKoinModuleLoader to handle scope closure
 *
 * @param unloadOnForgotten : unload loaded modules on onForgotten event
 * @param unloadOnAbandoned : unload loaded modules on onAbandoned event
 * @param unloadModules : unload loaded modules on onForgotten or onAbandoned event
 * @return true after modules are loaded, false if not.
 * @author Arnaud Giuliani
 */
@Composable
inline fun rememberKoinModules(
  unloadOnForgotten: Boolean? = null,
  unloadOnAbandoned: Boolean? = null,
  unloadModules: Boolean = false,
  crossinline modules: @DisallowComposableCalls () -> List<Module> = { emptyList() }
): Boolean {
  val koin = getKoin()
  val loadedState = remember(koin) { mutableStateOf(false) }

  remember(koin) {
    CompositionKoinModuleLoader(
      modules = modules(),
      koin = koin,
      unloadOnForgotten = unloadOnForgotten ?: unloadModules,
      unloadOnAbandoned = unloadOnAbandoned ?: unloadModules,
      onLoaded = { loadedState.value = true },
      onUnloaded = { loadedState.value = false },
    )
  }

  return loadedState.value
}

class CompositionKoinModuleLoader(
  private val modules: List<Module>,
  private val koin: Koin,
  private val unloadOnForgotten: Boolean,
  private val unloadOnAbandoned: Boolean,
  onLoaded: () -> Unit,
  private val onUnloaded: () -> Unit,
) : RememberObserver {

  init {
    koin.logger.debug("$this -> load modules")
    koin.loadModules(modules)
    onLoaded()
  }

  override fun onRemembered() {
    // Nothing to do
  }

  override fun onForgotten() {
    if (unloadOnForgotten) {
      unloadModules()
    }
  }

  override fun onAbandoned() {
    if (unloadOnAbandoned) {
      unloadModules()
    }
  }

  private fun unloadModules() {
    koin.logger.debug("$this -> unload modules")
    koin.unloadModules(modules)
    onUnloaded()
  }
}