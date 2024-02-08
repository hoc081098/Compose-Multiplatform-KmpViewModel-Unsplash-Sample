package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.presentation

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@Module
@ComponentScan
internal class PresentationModule

@Suppress("NOTHING_TO_INLINE")
internal inline fun presentationModule() = PresentationModule().module
