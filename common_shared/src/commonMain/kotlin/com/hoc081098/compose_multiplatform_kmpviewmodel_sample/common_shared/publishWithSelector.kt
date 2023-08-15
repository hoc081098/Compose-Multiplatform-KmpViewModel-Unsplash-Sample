package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import com.hoc081098.flowext.defer
import com.hoc081098.flowext.interval
import com.hoc081098.flowext.takeUntil
import com.hoc081098.flowext.timer
import com.hoc081098.flowext.withLatestFrom
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmField
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
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

private class SimpleSuspendLazy<T : Any>(
  initializer: suspend () -> T,
) {
  private val mutex = Mutex()

  private var _initializer: (suspend () -> T)? = initializer

  @Volatile
  private var value: T? = null

  suspend fun getValue(): T = value ?: mutex.withLock {
    value ?: _initializer!!().also {
      _initializer = null
      value = it
    }
  }
}

@OptIn(DelicateCoroutinesApi::class, InternalCoroutinesApi::class)
private class DefaultSelectorScope<T>(
  @JvmField val scope: CoroutineScope,
) :
  SynchronizedObject(),
  SelectorScope<T>,
  SelectorSharedFlowScope<T> {
  // Initialized in freezeAndInit
  private lateinit var channels: Array<Channel<T>>
  private lateinit var cachedOutputFlows: Array<SimpleSuspendLazy<Flow<Any?>>>

  @JvmField
  val blocks: MutableList<SelectorFunction<T, Any?>> = ArrayList()

  /**
   * Indicate that this scope is frozen, all [select] calls after this will throw [IllegalStateException]
   */
  @Volatile
  @JvmField
  var isFrozen = false

  /**
   * Indicate that a [select] calls is in progress,
   * all [select] calls inside another [select] block will throw [IllegalStateException]
   */
  @JvmField
  @Volatile
  var isInSelectClause = false

  override fun <R> select(block: SelectorFunction<T, R>): Flow<R> = synchronized(this) {
    check(!isInSelectClause) { "select can not be called inside another select" }
    check(!isFrozen) { "select only can be called inside publish, do not use SelectorScope outside publish" }

    isInSelectClause = true

    blocks += block
    val index = blocks.size - 1

    return defer {
      val cachedOutputFlowLazy = synchronized(this) {
        // Only frozen state can reach here,
        // that means we collect the output flow after frozen this scope
        check(isFrozen) { "only frozen state can reach here!" }
        cachedOutputFlows[index]
      }

      @Suppress("UNCHECKED_CAST") // Always safe
      cachedOutputFlowLazy.getValue() as Flow<R>
    }.also { isInSelectClause = false }
  }

  override fun Flow<T>.shared(replay: Int): SharedFlow<T> = kotlinXFlowShareIn(
    scope = scope,
    started = SharingStarted.Lazily,
    replay = replay,
  )

  fun freezeAndInit() = synchronized(this) {
    channels = Array(size) { Channel() }
    cachedOutputFlows = Array(size) { index ->
      val block = blocks[index]
      val flow = channels[index].consumeAsFlow()

      SimpleSuspendLazy { this.block(flow) }
    }

    isFrozen = true
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
    .collect(::println)
}
