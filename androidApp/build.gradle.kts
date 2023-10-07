plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.app)
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

  androidTarget()

  sourceSets {
    val androidMain by getting {
      dependencies {
        // Feature modules
        implementation(projects.features.featureSearchPhotoShared)
        implementation(projects.features.featurePhotoDetailShared)

        // Libraries
        implementation(projects.libraries.koinUtils)
        implementation(projects.libraries.koinComposeUtils)
        implementation(projects.libraries.coroutinesUtils)

        // Core
        implementation(projects.core.commonShared)
        implementation(projects.core.commonUiShared)
        implementation(projects.core.navigationShared)

        // Koin Android
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample"
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

  compileSdk =
    libs
      .versions
      .android
      .compile
      .map { it.toInt() }
      .get()
  defaultConfig {
    applicationId = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample"
    minSdk =
      libs
        .versions
        .android
        .min
        .map { it.toInt() }
        .get()
    targetSdk =
      libs
        .versions
        .android
        .target
        .map { it.toInt() }
        .get()
    versionCode = 1
    versionName = "1.0"
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

  packaging {
    // Copy from https://github.com/slackhq/slack-gradle-plugin/blob/cc5cb94272d610e68a3ee089d73a3a4794221d05/slack-plugin/src/main/kotlin/slack/gradle/StandardProjectConfigurations.kt#L682
    resources.excludes +=
      setOf(
        "META-INF/LICENSE.txt",
        "META-INF/LICENSE",
        "META-INF/NOTICE.txt",
        ".readme",
        "META-INF/maven/com.google.guava/guava/pom.properties",
        "META-INF/maven/com.google.guava/guava/pom.xml",
        "META-INF/DEPENDENCIES",
        "**/*.pro",
        "**/*.proto",
        // Weird bazel build metadata brought in by Tink
        "build-data.properties",
        "LICENSE_*",
        // We don't know where this comes from but it's 5MB
        // https://slack-pde.slack.com/archives/C8EER3C04/p1621353426001500
        "annotated-jdk/**",
        "META-INF/versions/9/previous-compilation-data.bin",
      )
  }
}
