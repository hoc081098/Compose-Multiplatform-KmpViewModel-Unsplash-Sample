plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
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
        api(compose.runtimeSaveable)

        api(libs.kmp.viewmodel)
        api(libs.kmp.viewmodel.savedstate)
        api(libs.kmp.viewmodel.compose)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val androidMain by getting {
      dependencies {
        // Khonshu
        api(libs.khonshu.navigation.compose)
      }
    }

    val nonAndroidMain by getting {
      dependencies {
        implementation(libs.uuid)
        implementation(libs.kotlinx.collections.immutable)
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.libraries.navigation"

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

  buildFeatures {
    buildConfig = false
  }
}
