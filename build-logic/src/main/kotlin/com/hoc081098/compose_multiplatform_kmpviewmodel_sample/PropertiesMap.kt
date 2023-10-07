package com.hoc081098.compose_multiplatform_kmpviewmodel_sample

import java.util.Properties
import org.gradle.api.Project

public interface PropertiesMap : Map<String, String> {
  override operator fun get(key: String): String
}

private class DefaultPropertiesMap(
  val inner: Map<String, String>,
) : PropertiesMap,
  Map<String, String> by inner {
  override fun get(key: String): String = inner[key] ?: error("Key $key not found")
}

private fun Map<String, String>.toPropertiesMap(): PropertiesMap = DefaultPropertiesMap(this)

fun Project.readPropertiesFile(pathFromRootProject: String): PropertiesMap =
  Properties()
    .apply {
      load(
        rootProject
          .file(pathFromRootProject)
          .apply {
            check(exists()) {
              "$pathFromRootProject file not found. " +
                "Create $pathFromRootProject file from root project."
            }
          }.reader(),
      )
    }.map { it.key as String to it.value as String }
    .toMap()
    .toPropertiesMap()
