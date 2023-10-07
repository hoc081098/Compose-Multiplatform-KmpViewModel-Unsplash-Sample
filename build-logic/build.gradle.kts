import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

group = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.build-logic"

kotlin {
  sourceSets {
    all {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
      }
    }
  }

  jvmToolchain {
    languageVersion.set(
      JavaLanguageVersion.of(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ),
    )
    vendor.set(JvmVendorSpec.AZUL)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  compilerOptions {
    // Use the same java toolchain version for compilation as the Kotlin plugin
    jvmTarget.set(
      JvmTarget.fromTarget(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ),
    )
  }
}

tasks.withType<JavaCompile>().configureEach {
  // Use the same java toolchain version for compilation as the Kotlin plugin
  sourceCompatibility =
    JavaVersion
      .toVersion(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ).toString()
  targetCompatibility =
    JavaVersion
      .toVersion(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ).toString()
}

dependencies {
  // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
  implementation(
    files(
      libs
        .javaClass
        .superclass
        .protectionDomain
        .codeSource
        .location,
    ),
  )
}

gradlePlugin {
  plugins {
    register("empty") {
      id = "compose_multiplatform_kmpviewmodel_sample.empty"
      implementationClass = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.EmptyPlugin"
    }
  }
}
