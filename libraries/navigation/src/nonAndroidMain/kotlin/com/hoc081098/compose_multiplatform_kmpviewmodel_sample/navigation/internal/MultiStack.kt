/**
 * Copied from [com.freeletics.khonshu.navigation.compose.internal.MultiStack.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/MultiStack.kt)
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.uuid4
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ContentDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import kotlinx.collections.immutable.ImmutableList

@InternalNavigationApi
internal class MultiStack(
  private val allStacks: MutableList<Stack>,
  private var startStack: Stack,
  private var currentStack: Stack,
  private val destinations: List<ContentDestination<*>>,
  private val onStackEntryRemoved: (StackEntry.Id) -> Unit,
  private val idGenerator: () -> String,
) {
  private val visibleEntryState: MutableState<ImmutableList<StackEntry<*>>> =
    mutableStateOf(currentStack.computeVisibleEntries())
  val visibleEntries: State<ImmutableList<StackEntry<*>>>
    get() = visibleEntryState

  private val canNavigateBackState: MutableState<Boolean> =
    mutableStateOf(canNavigateBack())
  val canNavigateBack: State<Boolean>
    get() = canNavigateBackState

  val startRoot = startStack.rootEntry.route as NavRoot

  fun <T : BaseRoute> entryFor(destinationId: DestinationId<T>): StackEntry<T>? {
    val entry = currentStack.entryFor(destinationId)
    if (entry != null) {
      return entry
    }

    // the root of the default back stack is always on the back stack
    if (startStack.rootEntry.destinationId == destinationId) {
      @Suppress("UNCHECKED_CAST")
      return startStack.rootEntry as StackEntry<T>
    }

    return null
  }

  private fun getBackStack(root: NavRoot): Stack? = allStacks.find { it.id == root.destinationId }

  private fun createBackStack(root: NavRoot): Stack {
    val newStack = Stack.createWith(root, destinations, onStackEntryRemoved, idGenerator)
    allStacks.add(newStack)
    return newStack
  }

  private fun removeBackStack(stack: Stack) {
    stack.clear()
    allStacks.remove(stack)
    onStackEntryRemoved(stack.rootEntry.id)
  }

  private fun updateVisibleDestinations() {
    visibleEntryState.value = currentStack.computeVisibleEntries()
    canNavigateBackState.value = canNavigateBack()
  }

  private fun canNavigateBack(): Boolean = currentStack.id != startStack.id || !currentStack.isAtRoot

  fun push(route: NavRoute) {
    currentStack.push(route)
    updateVisibleDestinations()
  }

  fun popCurrentStack() {
    currentStack.pop()
    updateVisibleDestinations()
  }

  fun pop() {
    if (currentStack.isAtRoot) {
      check(currentStack.id != startStack.id) {
        "Can't navigate back from the root of the start back stack"
      }
      removeBackStack(currentStack)
      currentStack = startStack
      // remove anything that the start stack could have shown before
      // can't use resetToRoot because that will also recreate the root
      currentStack.clear()
    } else {
      currentStack.pop()
    }
    updateVisibleDestinations()
  }

  fun <T : BaseRoute> popUpTo(
    destinationId: DestinationId<T>,
    isInclusive: Boolean,
  ) {
    currentStack.popUpTo(destinationId, isInclusive)
    updateVisibleDestinations()
  }

  fun push(
    root: NavRoot,
    clearTargetStack: Boolean,
  ) {
    val stack = getBackStack(root)
    currentStack =
      if (stack != null) {
        check(currentStack.id != stack.id) {
          "$root is already the current stack"
        }
        if (clearTargetStack) {
          removeBackStack(stack)
          createBackStack(root)
        } else {
          stack
        }
      } else {
        createBackStack(root)
      }
    if (stack?.id == startStack.id) {
      startStack = currentStack
    }
    updateVisibleDestinations()
  }

  fun resetToRoot(root: NavRoot) {
    when (root.destinationId) {
      startStack.id -> {
        if (currentStack.id != startStack.id) {
          removeBackStack(currentStack)
        }
        removeBackStack(startStack)
        val newStack = createBackStack(root)
        startStack = newStack
        currentStack = newStack
      }
      currentStack.id -> {
        removeBackStack(currentStack)
        val newStack = createBackStack(root)
        currentStack = newStack
      }
      else -> {
        error("$root is not on the current back stack")
      }
    }
    updateVisibleDestinations()
  }

  companion object {
    fun createWith(
      root: NavRoot,
      destinations: List<ContentDestination<*>>,
      onStackEntryRemoved: (StackEntry.Id) -> Unit,
      idGenerator: () -> String = { uuid4().toString() },
    ): MultiStack {
      val startStack = Stack.createWith(root, destinations, onStackEntryRemoved, idGenerator)
      return MultiStack(
        allStacks = mutableListOf(startStack),
        startStack = startStack,
        currentStack = startStack,
        destinations = destinations,
        onStackEntryRemoved = onStackEntryRemoved,
        idGenerator = idGenerator,
      )
    }
  }
}
