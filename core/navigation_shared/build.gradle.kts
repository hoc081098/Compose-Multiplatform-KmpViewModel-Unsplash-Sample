plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
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
        api(projects.libraries.navigation)
        compileOnly("org.jetbrains.compose.runtime:runtime:${org.jetbrains.compose.ComposeBuildConfig.composeVersion}")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.navigation_shared"

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
