plugins {
  alias(libs.plugins.kotlin.multiplatform)
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
        // KotlinX Coroutines
        api(libs.kotlinx.coroutines.core)

        // FlowExt
        api(libs.flow.ext)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
