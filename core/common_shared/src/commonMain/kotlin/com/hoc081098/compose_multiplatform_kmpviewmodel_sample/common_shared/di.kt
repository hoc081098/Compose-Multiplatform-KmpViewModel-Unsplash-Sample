package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.coroutines_utils.AppCoroutineDispatchers
import kotlin.jvm.JvmField
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@JvmField
val CommonModule =
  module {
    singleOf(::AppCoroutineDispatchers)
  }
