import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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

  jvm()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        // Compose
        implementation(compose.desktop.currentOs)
        implementation(compose.material3)

        // Core
        implementation(projects.core.commonShared)
        implementation(projects.core.commonUiShared)
        implementation(projects.core.navigationShared)

        // Libraries
        implementation(projects.libraries.koinUtils)
        implementation(projects.libraries.koinComposeUtils)
        implementation(projects.libraries.coroutinesUtils)

        // Feature modules
        implementation(projects.features.featureSearchPhotoShared)
        implementation(projects.features.featurePhotoDetailShared)

        implementation("org.apache.logging.log4j:log4j-api:2.24.1")
        implementation("org.apache.logging.log4j:log4j-core:2.24.1")
        implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.1")
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "KotlinMultiplatformComposeDesktopApplication"
      packageVersion = "1.0.0"
    }
  }
}
