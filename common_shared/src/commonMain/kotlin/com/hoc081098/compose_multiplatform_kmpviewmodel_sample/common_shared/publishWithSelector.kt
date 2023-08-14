package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import com.hoc081098.flowext.defer
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmField
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

sealed interface SelectorSourceFlow<T> : Flow<T>

@DslMarker
annotation class PublishSelectorDsl

@PublishSelectorDsl
sealed interface SelectorScope<T> {
  fun <R> select(block: SelectorSourceFlow<T>.() -> Flow<R>): Flow<R>

  fun <A> SelectorSourceFlow<A>.shared(replay: Int = 0): SharedFlow<A>
}

private class DefaultSelectorFlow<T>(@JvmField val flow: Flow<T>) : SelectorSourceFlow<T>, Flow<T> by flow

private class DefaultSelectorScope<T>(
  @JvmField val scope: CoroutineScope,
) : SelectorScope<T> {
  // Initialized in freezeAndInit
  // Guarded by mutex
  private lateinit var channels: Array<Channel<T>>
  private lateinit var selectorFlows: Array<DefaultSelectorFlow<T>>
  private lateinit var cachedSelectedFlows: Array<Flow<Any?>?>

  @JvmField
  val blocks: MutableList<(SelectorSourceFlow<T>) -> Flow<Any?>> = ArrayList()

  @Volatile
  @JvmField
  var isFrozen = false

  @Volatile
  @JvmField
  var isInSelectClause = false

  @JvmField
  val mutex = Mutex()

  override fun <R> select(block: (SelectorSourceFlow<T>) -> Flow<R>): Flow<R> {
    check(!isInSelectClause) { "select can not be called inside another select" }
    check(!isFrozen) { "select only can be called inside publish, do not use SelectorScope outside publish" }

    isInSelectClause = true

    blocks += block
    val index = blocks.size - 1

    return defer {
      val outputFlow = mutex.withLock {
        cachedSelectedFlows[index]
          ?: block(selectorFlows[index])
            .also { cachedSelectedFlows[index] = it }
      }

      @Suppress("UNCHECKED_CAST")
      outputFlow as Flow<R>
    }.also { isInSelectClause = false }
  }

  suspend inline fun freezeAndInit() {
    mutex.withLock {
      isFrozen = true

      channels = Array(size) { Channel() }
      selectorFlows = Array(size) { DefaultSelectorFlow(flow = channels[it].consumeAsFlow()) }
      cachedSelectedFlows = Array(size) { null }
    }
  }

  inline val size: Int get() = blocks.size

  suspend inline fun send(value: T) {
    for (channel in channels) {
      if (channel.isClosedForSend || channel.isClosedForReceive) {
        continue
      }

      try {
        channel.send(value)
      } catch (_: Throwable) {
        // Swallow all exceptions
      }
    }
  }

  fun close(e: Throwable?) {
    for (channel in channels) {
      channel.close(e)
    }
  }

  override fun <A> SelectorSourceFlow<A>.shared(replay: Int): SharedFlow<A> = shareIn(
    scope = scope,
    started = SharingStarted.Lazily,
    replay = replay,
  )

  fun cancel(e: CancellationException) {
    for (channel in channels) {
      channel.cancel(e)
    }
  }
}


fun <T, R> Flow<T>.publish(selector: SelectorScope<T>.() -> Flow<R>): Flow<R> {
  val source = this

  return flow {
    coroutineScope {
      val scope = DefaultSelectorScope<T>(this)
      val output = selector(scope)
      scope.freezeAndInit()

      launch {
        try {
          source.collect { value ->
            scope.send(value)
          }
          scope.close(null)
        } catch (e: CancellationException) {
          scope.cancel(e)
          throw e
        } catch (e: Throwable) {
          scope.close(e)
          throw e
        }
      }

      emitAll(output)
    }
  }
}

suspend fun main() {
  flow<Any?> {
    println("Collect...")
    emit(1)
    delay(100)
    emit(2)
    delay(100)
    emit(3)
    delay(100)
    emit("4")
  }
    .publish {
      merge(
        select { filterIsInstance<Int>().filter { it % 2 == 0 }.map { it to 1 } },
        select { filterIsInstance<Int>().filter { it % 2 != 0 }.map { it to 2 } },
        select { filterIsInstance<String>().map { it to 3 } },
      )
    }
    .collect(::println)
}
