plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  kotlin("multiplatform").apply(false)
  id("com.android.application").apply(false)
  id("com.android.library").apply(false)
  id("org.jetbrains.compose").apply(false)
  kotlin("plugin.serialization").apply(false)
  id("com.codingfeline.buildkonfig").apply(false)
  id("com.google.devtools.ksp").apply(false)
  id("com.diffplug.gradle.spotless").apply(false)
}

val ktlintVersion = "0.50.0"

allprojects {
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
