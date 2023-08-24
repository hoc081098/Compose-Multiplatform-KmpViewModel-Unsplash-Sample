plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

val koinVersion = "3.4.3"
val koinComposeVersion = "1.0.4"

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

        // Koin utils
        api(project(":libraries:koin-utils"))

        // Koin
        api("io.insert-koin:koin-core:$koinVersion")
        api("io.insert-koin:koin-compose:$koinComposeVersion")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
