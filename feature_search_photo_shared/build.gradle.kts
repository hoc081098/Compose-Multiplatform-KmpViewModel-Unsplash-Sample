import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("com.google.devtools.ksp")
}

val ktorVersion = "2.3.3"
val kotlinxSerializationVersion = "1.6.0-RC"
val coroutinesVersion = "1.7.3"
val kmpViewModel = "0.4.1-SNAPSHOT"
val koinVersion = "3.4.3"
val koinKspVersion = "1.2.2"
val arrowKtVersion = "1.2.0"

kotlin {
    androidTarget()

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api(project(":common_shared"))
                api(project(":common_ui_shared"))

                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                // KotlinX Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

                // KotlinX Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                // KMP View Model
                implementation("io.github.hoc081098:kmp-viewmodel:$kmpViewModel")
                implementation("io.github.hoc081098:kmp-viewmodel-savedstate:$kmpViewModel")
                implementation("io.github.hoc081098:kmp-viewmodel-compose:$kmpViewModel")

                // KotlinX DateTime
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                // Napier
                implementation("io.github.aakira:napier:2.6.1")

                // FlowExt
                implementation("io.github.hoc081098:FlowExt:0.7.1")

                // Koin
                api("io.insert-koin:koin-core:$koinVersion")
                implementation("io.insert-koin:koin-compose:1.0.4")
                implementation("io.insert-koin:koin-annotations:$koinKspVersion")

                // Arrow-kt
                implementation("io.arrow-kt:arrow-core:$arrowKtVersion")
                implementation("io.arrow-kt:arrow-fx-coroutines:$arrowKtVersion")

                // KotlinX Immutable Collections
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

                // Kamel Image
                implementation("media.kamel:kamel-image:0.7.1")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")

                // Ktor
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                // KotlinX Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

                // Koin
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("io.insert-koin:koin-androidx-compose:3.4.6")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                // Ktor
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)

                // Ktor
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                // KotlinX Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        buildConfig = true
    }
}

// ---------------------------- BUILD KONFIG ----------------------------

buildkonfig {
    packageName = "com.hoc081098.compose_multiplatform_kmpviewmodel_sample"
    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "UNSPLASH_CLIENT_ID",
            value = "none"
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "UNSPLASH_BASE_URL",
            value = "https://api.unsplash.com/"
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "FLAVOR",
            value = "none"
        )
    }

    defaultConfigs(flavor = "dev") {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "UNSPLASH_CLIENT_ID",
            value = rootProject.readPropertiesFile("local.properties")["UNSPLASH_CLIENT_ID_DEV"]
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "FLAVOR",
            value = "dev"
        )
    }
}

// ---------------------------- KOIN ANNOTATIONS PROCESSOR ----------------------------

dependencies {
    val koinKspCompiler = "io.insert-koin:koin-ksp-compiler:$koinKspVersion"
    add("kspCommonMainMetadata", koinKspCompiler)
//    add("kspAndroid", koinKspCompiler)
//    add("kspDesktop", koinKspCompiler)
//    add("kspIosX64", koinKspCompiler)
//    add("kspIosArm64", koinKspCompiler)
//    add("kspIosSimulatorArm64", koinKspCompiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// ---------------------------- UTILS ----------------------------

interface PropertiesMap : Map<String, String> {
    override operator fun get(key: String): String
}

private class DefaultPropertiesMap(val inner: Map<String, String>) : PropertiesMap, Map<String, String> by inner {
    override fun get(key: String): String = inner[key] ?: error("Key $key not found")
}

fun Map<String, String>.toPropertiesMap(): PropertiesMap = DefaultPropertiesMap(this)

fun Project.readPropertiesFile(pathFromRootProject: String): PropertiesMap = Properties().apply {
    load(
        rootProject
            .file(pathFromRootProject)
            .apply {
                check(exists()) {
                    "$pathFromRootProject file not found. " +
                        "Create $pathFromRootProject file from root project."
                }
            }
            .reader(),
    )
}
    .map { it.key as String to it.value as String }
    .toMap()
    .toPropertiesMap()