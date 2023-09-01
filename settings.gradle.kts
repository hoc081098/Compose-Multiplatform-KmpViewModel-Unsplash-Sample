enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "KmpViewModel-Compose-Multiplatform"

include(":androidApp")
include(":desktopApp")
include(":feature_search_photo_shared")
include(":feature_photo_detail_shared")
include(":common_ui_shared")
include(":navigation_shared")
include(":libraries:koin-utils")
include(":libraries:koin-compose-utils")
include(":libraries:coroutines-utils")
include(":libraries:compose-stable-wrappers")
include(":libraries:compose-lifecycle-utils")

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.6.0")
}
