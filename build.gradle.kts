plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.parcelize) apply false

  alias(libs.plugins.android.app) apply false
  alias(libs.plugins.android.library) apply false

  alias(
    libs
      .plugins
      .jetbrains
      .compose
      .mutiplatform,
  ) apply false

  alias(libs.plugins.buildkonfig) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.spotless) apply false
}

allprojects {
  configurations.all {
    resolutionStrategy.eachDependency { ->
      if (requested.group == "io.github.hoc081098") {
        // Check for updates every build
        resolutionStrategy.cacheChangingModulesFor(30, TimeUnit.MINUTES)
      }
    }
  }

  apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
  configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    val ktlintVersion =
      rootProject
        .libs
        .versions
        .ktlint
        .get()

    kotlin {
      target("**/*.kt")
      targetExclude("**/build/**/*.kt", "**/.gradle/**/*.kt")

      ktlint(ktlintVersion)
        .setEditorConfigPath(rootProject.file(".editorconfig"))

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    format("xml") {
      target("**/res/**/*.xml")
      targetExclude("**/build/**/*.xml", "**/.idea/**/*.xml", "**/.gradle/**/*.xml")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
      lineEndings =
        com
          .diffplug
          .spotless
          .LineEnding
          .UNIX
    }

    kotlinGradle {
      target("**/*.gradle.kts", "*.gradle.kts")
      targetExclude("**/build/**/*.kts", "**/.gradle/**/*.kts")

      ktlint(ktlintVersion)
        .setEditorConfigPath(rootProject.file(".editorconfig"))

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }
  }
}
