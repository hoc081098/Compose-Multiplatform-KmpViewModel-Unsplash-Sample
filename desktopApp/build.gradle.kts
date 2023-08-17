import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

kotlin {
  jvm()
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(project(":feature_search_photo_shared"))
        implementation(project(":feature_photo_detail_shared"))

        implementation("org.slf4j:slf4j-reload4j:2.0.7")
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
