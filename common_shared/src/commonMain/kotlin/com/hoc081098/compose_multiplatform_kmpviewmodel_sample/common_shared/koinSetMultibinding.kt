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
internal val SetLock = SynchronizedObject()

class SetMultibinding<V> : MutableSet<V> by linkedSetOf<V>()

inline fun <reified V> defaultSetMultibindingQualifier(): StringQualifier =
  named("${V::class}")

inline fun <reified V> Module.declareSetMultibinding(
  qualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
) = single(qualifier = qualifier) { SetMultibinding<V>() }

@Suppress("RedundantUnitExpression") // Keep for readability
@OptIn(InternalCoroutinesApi::class)
inline fun <reified V> Module.intoSetMultibinding(
  key: V,
  multibindingQualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
  crossinline definition: Definition<V>,
) {
  var multibinding = null as SetMultibinding<V>?

  single<Unit>(
    qualifier = named("${multibindingQualifier.value}_$key"),
    createdAtStart = true,
  ) {
    synchronized(SetLock) {
      multibinding = get(multibindingQualifier)
      multibinding!! += definition(it)
    }
    Unit
  }.onClose {
    synchronized(SetLock) {
      multibinding?.remove(key)
    }
  }
}

inline fun <reified V> Scope.getSetMultibinding(
  qualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
): Set<V> =
  get<SetMultibinding<V>>(qualifier = qualifier).toSet()

@Composable
inline fun <reified V> koinInjectSetMultibinding(
  qualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
  scope: Scope = LocalKoinScope.current,
): Set<V> = remember(scope, qualifier) {
  scope.getSetMultibinding(qualifier = qualifier)
}
