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
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
