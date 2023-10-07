/**
 * Copied from [com.freeletics.khonshu.navigation.compose.internal.Stack.kt](https://github.com/freeletics/khonshu/blob/de0e8f812d89303ac347119d5c448bc40224d4f2/navigation-experimental/src/main/kotlin/com/freeletics/khonshu/navigation/compose/internal/Stack.kt).
 */

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.internal

import com.benasher44.uuid.uuid4
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.BaseRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ContentDestination
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoot
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.NavRoute
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation.ScreenDestination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.collections.immutable.toImmutableList

@InternalNavigationApi
internal class Stack private constructor(
  initialStack: List<StackEntry<*>>,
  private val destinations: List<ContentDestination<*>>,
  private val onStackEntryRemoved: (StackEntry.Id) -> Unit,
  private val idGenerator: () -> String,
) {
  private val stack =
    ArrayDeque<StackEntry<*>>(20).also {
      it.addAll(initialStack)
    }

  val id: DestinationId<*> get() = rootEntry.destinationId
  val rootEntry: StackEntry<*> get() = stack.first()
  val isAtRoot: Boolean get() = !stack.last().removable

  @Suppress("UNCHECKED_CAST")
  fun <T : BaseRoute> entryFor(destinationId: DestinationId<T>): StackEntry<T>? =
    stack.findLast { it.destinationId == destinationId } as StackEntry<T>?

  fun computeVisibleEntries(): ImmutableList<StackEntry<*>> {
    if (stack.size == 1) {
      return stack.toImmutableList()
    }

    // go through the stack from the top until reaching the first ScreenDestination
    // then create a List of the elements starting from there
    val iterator = stack.listIterator(stack.size)
    while (iterator.hasPrevious()) {
      if (iterator.previous().destination is ScreenDestination<*>) {
        val expectedSize = stack.size - iterator.nextIndex()
        return ArrayList<StackEntry<*>>(expectedSize)
          .apply {
            while (iterator.hasNext()) {
              add(iterator.next())
            }
          }.let(::ImmutableListAdapter)
      }
    }

    error("Stack did not contain a ScreenDestination $stack")
  }

  fun push(route: NavRoute) {
    stack.add(entry(route, destinations, idGenerator))
  }

  fun pop() {
    check(stack.last().removable) { "Can't pop the root of the back stack" }
    popInternal()
  }

  private fun popInternal() {
    val entry = stack.removeLast()
    onStackEntryRemoved(entry.id)
  }

  fun popUpTo(
    destinationId: DestinationId<*>,
    isInclusive: Boolean,
  ) {
    while (stack.last().destinationId != destinationId) {
      check(stack.last().removable) { "Route ${destinationId.route} not found on back stack" }
      popInternal()
    }

    if (isInclusive) {
      // using pop here to get the default removable check
      pop()
    }
  }

  fun clear() {
    while (stack.last().removable) {
      popInternal()
    }
  }

  companion object {
    fun createWith(
      root: NavRoot,
      destinations: List<ContentDestination<*>>,
      onStackEntryRemoved: (StackEntry.Id) -> Unit,
      idGenerator: () -> String = { uuid4().toString() },
    ): Stack {
      val rootEntry = entry(root, destinations, idGenerator)
      return Stack(listOf(rootEntry), destinations, onStackEntryRemoved, idGenerator)
    }

    private inline fun <T : BaseRoute> entry(
      route: T,
      destinations: List<ContentDestination<*>>,
      idGenerator: () -> String,
    ): StackEntry<T> {
      @Suppress("UNCHECKED_CAST")
      val destination = destinations.find { it.id == route.destinationId } as ContentDestination<T>
      return StackEntry(StackEntry.Id(idGenerator()), route, destination)
    }

    private const val SAVED_STATE_IDS = "com.freeletics.khonshu.navigation.stack.ids"
    private const val SAVED_STATE_ROUTES = "com.freeletics.khonshu.navigation.stack.routes"
  }
}
