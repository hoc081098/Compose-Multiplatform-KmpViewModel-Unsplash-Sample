plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

val ktorVersion = "2.3.3"
val kotlinxSerializationVersion = "1.6.0-RC"
val coroutinesVersion = "1.7.3"
val kmpViewModel = "0.4.1-SNAPSHOT"
val koinVersion = "3.4.3"
val koinKspVersion = "1.2.2"
val arrowKtVersion = "1.2.0"

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  jvmToolchain(17)

  targetHierarchy.default {
    common {
      group("nonAndroid") {
        withJvm()
        withIos()
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
  compileSdk = (findProperty("android.compileSdk") as String).toInt()
  namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.common_shared"

  defaultConfig {
    minSdk = (findProperty("android.minSdk") as String).toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {}
}
