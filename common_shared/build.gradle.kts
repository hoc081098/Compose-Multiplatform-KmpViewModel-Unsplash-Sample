plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
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

  targetHierarchy.default()

  jvm()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(compose.runtime)

        // Koin
        api("io.insert-koin:koin-core:$koinVersion")
        api("io.insert-koin:koin-compose:1.0.4")

        // Napier
        api("io.github.aakira:napier:2.6.1")

        // KotlinX Coroutines
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

        // FlowExt
        api("io.github.hoc081098:FlowExt:0.7.1")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
