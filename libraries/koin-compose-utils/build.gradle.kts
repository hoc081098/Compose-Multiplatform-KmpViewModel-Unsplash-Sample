plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(
    libs
      .plugins
      .jetbrains
      .compose
      .mutiplatform,
  )
}

@OptIn(
  org
    .jetbrains
    .kotlin
    .gradle
    .ExperimentalKotlinGradlePluginApi::class,
)
kotlin {
  jvmToolchain {
    languageVersion.set(
      JavaLanguageVersion.of(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ),
    )
    vendor.set(JvmVendorSpec.AZUL)
  }

  targetHierarchy.default()

  jvm()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(compose.runtime)
        api(compose.runtimeSaveable)

        // Koin utils
        api(projects.libraries.koinUtils)

        // Navigation
        api(projects.libraries.navigation)

        // Koin
        api(libs.koin.core)
        api(libs.koin.compose)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
