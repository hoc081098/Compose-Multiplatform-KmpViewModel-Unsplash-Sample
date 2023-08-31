plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
}

val coroutinesVersion = "1.7.3"

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  jvmToolchain(17)

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
        jvmTarget = JavaVersion.VERSION_17.toString()
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
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val androidMain by getting {
      dependencies {
        // AndroidX Lifecycle Compose Runtime
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
      }
    }
  }
}

android {
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.compose-lifecycle-utils"
  compileSdk = (findProperty("android.compileSdk") as String).toInt()
  defaultConfig {
    minSdk = (findProperty("android.minSdk") as String).toInt()
  }

  buildFeatures {
    buildConfig = false
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
