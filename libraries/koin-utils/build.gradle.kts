plugins {
  kotlin("multiplatform")
}

val koinVersion = "3.4.3"
val atomicfuVersion = "0.22.0"

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
        // Koin
        api("io.insert-koin:koin-core:$koinVersion")

        // AtomicFu
        api("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
