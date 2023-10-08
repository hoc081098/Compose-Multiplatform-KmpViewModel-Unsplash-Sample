enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val copyToBuildLogic = { sourcePath: String ->
  rootDir.resolve(sourcePath).copyRecursively(
    target = rootDir.resolve("build-logic").resolve(sourcePath),
    overwrite = true,
  )
  println("[DONE] copied $sourcePath")
}
arrayOf("gradle.properties", "gradle/wrapper").forEach(copyToBuildLogic)

rootProject.name = "KmpViewModel-Compose-Multiplatform"

include(":androidApp")
include(":desktopApp")
include(":features:feature_search_photo_shared")
include(":features:feature_photo_detail_shared")
include(":core:common_shared")
include(":core:common_ui_shared")
include(":core:navigation_shared")
include(":libraries:navigation")
include(":libraries:koin-utils")
include(":libraries:koin-compose-utils")
include(":libraries:coroutines-utils")
include(":libraries:compose-stable-wrappers")
include(":libraries:compose-lifecycle-utils")

pluginManagement {
  includeBuild("build-logic")

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
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}
