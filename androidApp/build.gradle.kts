plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

val koinVersion = "3.4.3"

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":feature_search_photo_shared"))

                // Koin Android
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("io.insert-koin:koin-androidx-compose:3.4.6")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

    packagingOptions {
        exclude("META-INF/versions/9/previous-compilation-data.bin")
    }
}
