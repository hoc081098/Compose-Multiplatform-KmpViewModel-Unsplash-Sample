plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.parcelize) apply false

  alias(libs.plugins.android.app) apply false
  alias(libs.plugins.android.library) apply false

  alias(libs.plugins.jetbrains.compose.mutiplatform) apply false

  alias(libs.plugins.buildkonfig) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.spotless) apply false
}

val ktlintVersion = libs.versions.ktlint.get()

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
    kotlin {
      target("**/*.kt")

      ktlint(ktlintVersion)

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    format("xml") {
      target("**/res/**/*.xml")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    kotlinGradle {
      target("**/*.gradle.kts", "*.gradle.kts")

      ktlint(ktlintVersion)

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }
  }
}
