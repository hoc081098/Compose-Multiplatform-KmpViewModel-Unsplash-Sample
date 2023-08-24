import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

val koinVersion = "3.4.3"
val koinComposeVersion = "1.0.4"

kotlin {
  jvm()
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(compose.material3)

        implementation(project(":feature_search_photo_shared"))
        implementation(project(":feature_photo_detail_shared"))
        implementation(project(":libraries:coroutines-utils"))

        implementation("org.apache.logging.log4j:log4j-api:2.20.0")
        implementation("org.apache.logging.log4j:log4j-core:2.20.0")
        implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

        implementation("io.insert-koin:koin-core:$koinVersion")
        implementation("io.insert-koin:koin-compose:$koinComposeVersion")
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
