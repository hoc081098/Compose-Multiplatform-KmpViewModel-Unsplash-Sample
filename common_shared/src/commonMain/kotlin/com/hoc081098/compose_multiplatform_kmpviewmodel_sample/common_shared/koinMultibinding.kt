package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.jvm.JvmField
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import org.koin.compose.LocalKoinScope
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.onClose

@OptIn(InternalCoroutinesApi::class)
@PublishedApi
@JvmField
internal val Lock = SynchronizedObject()

class Multibinding<K, V> : MutableMap<K, V> by mutableMapOf()

inline fun <reified K, reified V> defaultMultibindingQualifier(): StringQualifier =
  named("${K::class}_${V::class}")

inline fun <reified K, reified V> Module.declareMultibinding(
  qualifier: StringQualifier = defaultMultibindingQualifier<K, V>(),
) = single(qualifier = qualifier) { Multibinding<K, V>() }

@Suppress("RedundantUnitExpression") // Keep for readability
@OptIn(InternalCoroutinesApi::class)
inline fun <reified K, reified V> Module.intoMultibinding(
  key: K,
  multibindingQualifier: StringQualifier = defaultMultibindingQualifier<K, V>(),
  crossinline definition: Definition<V>,
) {
  var multibinding = null as Multibinding<K, V>?

  single<Unit>(
    qualifier = named("${multibindingQualifier.value}_$key"),
    createdAtStart = true,
  ) {
    synchronized(Lock) {
      multibinding = get(multibindingQualifier)
      multibinding!![key] = definition(it)
    }
    Unit
  }.onClose {
    synchronized(Lock) {
      multibinding?.remove(key)
    }
  }
}

inline fun <reified K, reified V> Scope.getMultibinding(
  qualifier: StringQualifier = defaultMultibindingQualifier<K, V>(),
): Map<K, V> =
  get<Multibinding<K, V>>(qualifier = qualifier).toMap()

@Composable
inline fun <reified K, reified V> koinInjectMultibinding(
  qualifier: StringQualifier = defaultMultibindingQualifier<K, V>(),
  scope: Scope = LocalKoinScope.current,
): Map<K, V> = remember(scope, qualifier) {
  scope.getMultibinding(qualifier = qualifier)
}
