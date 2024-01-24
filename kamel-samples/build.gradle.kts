import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.com.android.application)
    kotlin("native.cocoapods")
}

android {
    compileSdk = 34
    namespace = "io.kamel.samples"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }

    sourceSets["main"].resources.srcDir("src/commonMain/resources")
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    androidTarget()
    jvm("desktop")
    js(IR) {
        browser()
        binaries.executable()
    }
    fun iosTargets(config: KotlinNativeTarget.() -> Unit) {
        iosArm64(config)
        iosSimulatorArm64(config)
        iosX64(config)
    }
    iosTargets {
        binaries.forEach {
            it.apply {
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    fun macosTargets(config: KotlinNativeTarget.() -> Unit) {
        macosX64(config)
        macosArm64(config)
    }
    macosTargets {
        binaries.executable {
            freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal"
            )
            linkerOpts.add("-lsqlite3")
        }
    }
    applyDefaultHierarchyTemplate()

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/Kamel-Media/Kamel"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../kamel-sample-ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":kamel-image"))
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.compose.components.resources)
                implementation(libs.okio)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.google.android.material)
                implementation(libs.ktor.client.android)
                implementation(libs.slf4j)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.cio)
                implementation(libs.slf4j)
            }
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

    }
}

compose {
    desktop {
        application {
            mainClass = "io.kamel.samples.DesktopSampleKt"
        }
    }
}

compose.experimental {
    web.application {}
}


compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosArm64"))
    distributions {
        targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg)
        packageName = "Native-Sample"
        packageVersion = "1.0.0"
    }
}
