import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.isCiBuild
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.readPropertiesFile

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
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.buildkonfig)
  alias(libs.plugins.ksp)
  id("compose_multiplatform_kmpviewmodel_sample.empty")
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

  jvm("desktop")

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

      dependencies {
        // Compose
        api(compose.runtime)
        api(compose.foundation)
        api(compose.material3)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)

        // Core and Libraries
        api(projects.core.commonUiShared)
        api(projects.core.navigationShared)
        implementation(projects.libraries.koinComposeUtils)
        implementation(projects.libraries.coroutinesUtils)
        implementation(projects.libraries.composeStableWrappers)
        implementation(projects.libraries.composeLifecycleUtils)

        // Ktor
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.serialization)
        implementation(
          libs
            .ktor
            .client
            .content
            .negotiation,
        )
        implementation(
          libs
            .ktor
            .serialization
            .kotlinx
            .json,
        )

        // KotlinX Serialization
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)

        // KotlinX Coroutines
        implementation(libs.kotlinx.coroutines.core)

        // KMP View Model
        implementation(libs.kmp.viewmodel)
        implementation(libs.kmp.viewmodel.savedstate)
        implementation(libs.kmp.viewmodel.compose)

        // FlowExt
        implementation(libs.flow.ext)

        // Koin
        implementation(libs.koin.annotations)
        implementation(libs.koin.core)
        implementation(libs.koin.compose)

        // Arrow-kt
        implementation(libs.arrow.core)
        implementation(libs.arrow.fx.coroutines)

        // KotlinX Utils
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.kotlinx.datetime)

        // Kamel Image
        implementation(libs.kamel.image)

        // Napier
        api(libs.napier)
      }
    }
    val androidMain by getting {
      dependencies {
        api(libs.androidx.activity.compose)
        api(libs.androidx.appcompat)
        api(libs.androidx.core)

        // Ktor
        implementation(libs.ktor.client.okhttp)

        // KotlinX Coroutines
        implementation(libs.kotlinx.coroutines.android)
      }
    }
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)

      dependencies {
        // Ktor
        implementation(libs.ktor.client.darwin)
      }
    }
    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.common)

        // Ktor
        implementation(libs.ktor.client.java)

        // KotlinX Coroutines
        implementation(libs.kotlinx.coroutines.swing)
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.features.photo_detail"

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

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
    buildConfig = true
  }
}

// ---------------------------- BUILD KONFIG ----------------------------

buildkonfig {
  packageName = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail"
  defaultConfigs {
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "UNSPLASH_CLIENT_ID",
      value = "none",
    )
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "UNSPLASH_BASE_URL",
      value = "https://api.unsplash.com/",
    )
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "FLAVOR",
      value = "none",
    )
  }

  defaultConfigs(flavor = "dev") {
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "UNSPLASH_CLIENT_ID",
      value =
        if (isCiBuild) {
          logger.info("CI build, ignore checking existence of local.properties file")
          "none"
        } else {
          rootProject.readPropertiesFile("local.properties")["UNSPLASH_CLIENT_ID_DEV"]
        },
    )
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "FLAVOR",
      value = "dev",
    )
  }
}

// ---------------------------- KOIN ANNOTATIONS PROCESSOR ----------------------------

dependencies {
  add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}
