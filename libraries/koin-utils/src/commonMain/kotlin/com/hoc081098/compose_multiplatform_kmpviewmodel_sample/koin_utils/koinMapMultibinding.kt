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

class MapMultibinding<K, V> {
  private val map = atomic(emptyMap<K, V>())

  @InternalKoinMultibindingApi
  @PublishedApi
  internal fun remove(key: K) {
    map.loop { current ->
      when (key) {
        in current -> {
          if (map.compareAndSet(current, current - key)) {
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
  internal operator fun set(
    key: K,
    value: V,
  ) {
    map.loop { current ->
      if (map.compareAndSet(current, current + (key to value))) {
        // exit loop
        return
      }
    }
  }

  @InternalKoinMultibindingApi
  @PublishedApi
  internal val asMap: Map<K, V> get() = map.value
}

inline fun <reified K, reified V> defaultMapMultibindingQualifier(): StringQualifier =
  named("MapMultibinding<${K::class.getFullName()},${V::class.getFullName()}>")

inline fun <reified K, reified V> Module.declareMapMultibinding(qualifier: StringQualifier = defaultMapMultibindingQualifier<K, V>()) =
  single(qualifier = qualifier) { MapMultibinding<K, V>() }

@OptIn(InternalKoinMultibindingApi::class)
@Suppress("RedundantUnitExpression", "RemoveExplicitTypeArguments") // Keep for readability
inline fun <reified K, reified V> Module.intoMapMultibinding(
  key: K,
  multibindingQualifier: StringQualifier = defaultMapMultibindingQualifier<K, V>(),
  crossinline definition: Definition<V>,
) {
  var multibinding by atomic<MapMultibinding<K, V>?>(null)

  single<Unit>(
    qualifier = named("${multibindingQualifier.value}::$key"),
    createdAtStart = true,
  ) {
    multibinding =
      get<MapMultibinding<K, V>>(multibindingQualifier).apply {
        this[key] = definition(it)
      }
    Unit
  }.onClose {
    multibinding?.remove(key)
  }
}

@OptIn(InternalKoinMultibindingApi::class)
inline fun <reified K, reified V> Scope.getMapMultibinding(
  qualifier: StringQualifier = defaultMapMultibindingQualifier<K, V>(),
): Map<K, V> = get<MapMultibinding<K, V>>(qualifier = qualifier).asMap
