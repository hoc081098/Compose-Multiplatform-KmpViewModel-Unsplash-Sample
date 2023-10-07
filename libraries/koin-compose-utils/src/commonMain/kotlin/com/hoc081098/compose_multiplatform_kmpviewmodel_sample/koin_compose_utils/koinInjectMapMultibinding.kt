package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.defaultMapMultibindingQualifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.getMapMultibinding
import org.koin.compose.getKoinScope
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope

@Composable
inline fun <reified K, reified V> koinInjectMapMultibinding(
  qualifier: StringQualifier = defaultMapMultibindingQualifier<K, V>(),
  scope: Scope = getKoinScope(),
): Map<K, V> =
  remember(scope, qualifier) {
    scope.getMapMultibinding(qualifier = qualifier)
  }
