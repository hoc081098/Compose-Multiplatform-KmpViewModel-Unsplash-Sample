package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import com.hoc081098.flowext.defer
import com.hoc081098.flowext.interval
import com.hoc081098.flowext.materialize
import com.hoc081098.flowext.takeUntil
import com.hoc081098.flowext.timer
import com.hoc081098.flowext.withLatestFrom
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmField
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
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
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn as kotlinXFlowShareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@DslMarker
annotation class PublishSelectorDsl

@PublishSelectorDsl
sealed interface SelectorSharedFlowScope<T> {
  @PublishSelectorDsl
  fun Flow<T>.shared(replay: Int = 0): SharedFlow<T>

  /** @suppress */
  @Deprecated(
    level = DeprecationLevel.ERROR,
    message = "This function is not supported",
    replaceWith = ReplaceWith("this.shared(replay)"),
  )
  fun <T> Flow<T>.shareIn(
    scope: CoroutineScope,
    started: SharingStarted,
    replay: Int = 0,
  ): SharedFlow<T> = throw UnsupportedOperationException("Not implemented, should not be called")
}

typealias SelectorFunction<T, R> = suspend SelectorSharedFlowScope<T>.(Flow<T>) -> Flow<R>

@PublishSelectorDsl
sealed interface SelectorScope<T> {
  @PublishSelectorDsl
  fun <R> select(block: SelectorFunction<T, R>): Flow<R>
}

@OptIn(DelicateCoroutinesApi::class)
private class DefaultSelectorScope<T>(
  @JvmField val scope: CoroutineScope,
) : SelectorScope<T>, SelectorSharedFlowScope<T> {
  // Initialized in freezeAndInit
  // Guarded by mutex
  private lateinit var channels: Array<Channel<T>>
  private lateinit var selectorFlows: Array<Flow<T>>
  private lateinit var cachedOutputFlows: Array<Flow<Any?>?>

  @JvmField
  val mutex = Mutex()

  @JvmField
  val blocks: MutableList<SelectorFunction<T, Any?>> = ArrayList()

  @Volatile
  @JvmField
  var isFrozen = false

  @Volatile
  @JvmField
  var isInSelectClause = false

  override fun <R> select(block: SelectorFunction<T, R>): Flow<R> {
    check(!isInSelectClause) { "select can not be called inside another select" }
    check(!isFrozen) { "select only can be called inside publish, do not use SelectorScope outside publish" }

    isInSelectClause = true

    blocks += block
    val index = blocks.size - 1

    return defer {
      // Only frozen state can reach here,
      // that means we collect the output flow after frozen this scope
      check(isFrozen) { "only frozen state can reach here!" }

      val outputFlow = mutex.withLock {
        cachedOutputFlows[index]
          ?: block(this@DefaultSelectorScope, selectorFlows[index])
            .also { cachedOutputFlows[index] = it }
      }

      @Suppress("UNCHECKED_CAST") // Always safe
      outputFlow as Flow<R>
    }.also { isInSelectClause = false }
  }

  override fun Flow<T>.shared(replay: Int): SharedFlow<T> = kotlinXFlowShareIn(
    scope = scope,
    started = SharingStarted.Lazily,
    replay = replay,
  )

  suspend inline fun freezeAndInit() {
    mutex.withLock {
      isFrozen = true

      channels = Array(size) { Channel() }
      selectorFlows = Array(size) { channels[it].consumeAsFlow() }
      cachedOutputFlows = Array(size) { null }
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

  fun cancel(e: CancellationException) {
    for (channel in channels) {
      channel.cancel(e)
    }
  }
}

fun <T, R> Flow<T>.publish(selector: suspend SelectorScope<T>.() -> Flow<R>): Flow<R> {
  val source = this

  return flow {
    coroutineScope {
      val scope = DefaultSelectorScope<T>(this)

      val output = selector(scope)

      // IMPORTANT: freeze and init before collect the output flow
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

      // IMPORTANT: collect the output flow after frozen the scope
      emitAll(output)
    }
  }
}

suspend fun main() {
  flow<Any?> {
    println("Collect...")
    delay(100)
    emit(1)
    delay(100)
    emit(2)
    delay(100)
    emit(3)
    delay(100)
    emit("4")
  }
    .publish {
      delay(100)

      merge(
        select { flow ->
          delay(1)
          val sharedFlow = flow.shared()

          interval(0, 100)
            .onEach { println(">>> interval: $it") }
            .flatMapMerge { value ->
              timer(value, 50)
                .withLatestFrom(sharedFlow)
                .map { it to "shared" }
            }
            .takeUntil(sharedFlow.filter { it == 3 })
        },
        select { flow ->
          flow.filterIsInstance<Int>()
            .filter { it % 2 == 0 }
            .map { it to "even" }
        },
        select { flow ->
          flow.filterIsInstance<Int>()
            .filter { it % 2 != 0 }
            .map { it to "odd" }
        },
        select { flow ->
          flow.filterIsInstance<String>()
            .map { it to "string" }
        },
      )
    }
    .materialize()
    .collect(::println)
}
