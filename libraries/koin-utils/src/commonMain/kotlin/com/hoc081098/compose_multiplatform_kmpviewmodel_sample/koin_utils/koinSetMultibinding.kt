package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.loop
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.onClose
import org.koin.ext.getFullName

class SetMultibinding<V> {
  private val set = atomic(emptySet<V>())

  @InternalKoinMultibindingApi
  @PublishedApi
  internal fun remove(value: V) {
    set.loop { current ->
      when (value) {
        in current -> {
          if (set.compareAndSet(current, current - value)) {
            return
          }
        }

        else -> {
          // exit loop
          return
        }
      }
    }
  }

  @InternalKoinMultibindingApi
  @PublishedApi
  internal operator fun plusAssign(value: V) {
    set.loop { current ->
      if (set.compareAndSet(current, current + value)) {
        // exit loop
        return
      }
    }
  }

  @InternalKoinMultibindingApi
  @PublishedApi
  internal val asSet: Set<V> get() = set.value
}

inline fun <reified V> defaultSetMultibindingQualifier(): StringQualifier = named("SetMultibinding<${V::class.getFullName()}>")

inline fun <reified V> Module.declareSetMultibinding(qualifier: StringQualifier = defaultSetMultibindingQualifier<V>()) =
  single(qualifier = qualifier) { SetMultibinding<V>() }

@OptIn(InternalKoinMultibindingApi::class)
@Suppress("RedundantUnitExpression", "RemoveExplicitTypeArguments") // Keep for readability
inline fun <reified V> Module.intoSetMultibinding(
  key: V,
  multibindingQualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
  crossinline definition: Definition<V>,
) {
  var multibinding by atomic<SetMultibinding<V>?>(null)

  single<Unit>(
    qualifier = named("${multibindingQualifier.value}::$key"),
    createdAtStart = true,
  ) {
    multibinding =
      get<SetMultibinding<V>>(multibindingQualifier).apply {
        this += definition(it)
      }
    Unit
  }.onClose {
    multibinding?.remove(key)
  }
}

@OptIn(InternalKoinMultibindingApi::class)
inline fun <reified V> Scope.getSetMultibinding(qualifier: StringQualifier = defaultSetMultibindingQualifier<V>()): Set<V> =
  get<SetMultibinding<V>>(qualifier = qualifier).asSet
