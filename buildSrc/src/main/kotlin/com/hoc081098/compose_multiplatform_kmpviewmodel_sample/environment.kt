package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import org.gradle.api.Project

val Project.isCiBuild: Boolean
  get() = providers.environmentVariable("CI").orNull == "true"

fun Project.envOrProp(name: String): String =
  providers.environmentVariable(name).orNull
    ?: providers.gradleProperty(name).getOrElse("")
