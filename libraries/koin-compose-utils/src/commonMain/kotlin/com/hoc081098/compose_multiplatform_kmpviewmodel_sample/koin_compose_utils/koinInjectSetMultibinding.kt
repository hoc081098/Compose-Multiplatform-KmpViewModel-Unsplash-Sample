package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.defaultSetMultibindingQualifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_utils.getSetMultibinding
import org.koin.compose.getKoinScope
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope

@Composable
inline fun <reified V> koinInjectSetMultibinding(
  qualifier: StringQualifier = defaultSetMultibindingQualifier<V>(),
  scope: Scope = getKoinScope(),
): Set<V> =
  remember(scope, qualifier) {
    scope.getSetMultibinding(qualifier = qualifier)
  }
