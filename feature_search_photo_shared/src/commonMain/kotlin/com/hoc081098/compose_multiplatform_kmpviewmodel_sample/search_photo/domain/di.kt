package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@Module
@ComponentScan
internal class DomainModule

@Suppress("NOTHING_TO_INLINE")
internal inline fun domainModule() = DomainModule().module
