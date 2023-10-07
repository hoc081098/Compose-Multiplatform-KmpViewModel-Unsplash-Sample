plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)
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

  targetHierarchy.default {
    common {
      group("nonAndroid") {
        withJvm()
        withNative()
      }
    }
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget =
          JavaVersion
            .toVersion(
              libs
                .versions
                .java
                .target
                .get(),
            ).toString()
      }
    }
  }

  jvm()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(compose.runtime)

        // KotlinX Coroutines
        api(libs.kotlinx.coroutines.core)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val androidMain by getting {
      dependencies {
        // AndroidX Lifecycle Runtime Compose
        api(
          libs
            .androidx
            .lifecycle
            .runtime
            .compose,
        )
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.libraries.compose-lifecycle-utils"

  compileSdk =
    libs
      .versions
      .android
      .compile
      .map { it.toInt() }
      .get()
  defaultConfig {
    minSdk =
      libs
        .versions
        .android
        .min
        .map { it.toInt() }
        .get()
  }

  buildFeatures {
    buildConfig = false
  }

  compileOptions {
    sourceCompatibility =
      JavaVersion.toVersion(
        libs
          .versions
          .java
          .target
          .get(),
      )
    targetCompatibility =
      JavaVersion.toVersion(
        libs
          .versions
          .java
          .target
          .get(),
      )
  }
}
