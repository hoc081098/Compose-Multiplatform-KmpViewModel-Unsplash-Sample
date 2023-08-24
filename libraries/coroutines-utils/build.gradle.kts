plugins {
  kotlin("multiplatform")
}

val coroutinesVersion = "1.7.3"
val flowExtVersion = "0.7.1"

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
        // KotlinX Coroutines
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

        // FlowExt
        api("io.github.hoc081098:FlowExt:$flowExtVersion")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
