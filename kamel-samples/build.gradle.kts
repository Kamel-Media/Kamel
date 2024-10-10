import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.application)
    kotlin("native.cocoapods")
}

android {
    compileSdk = 34
    namespace = "io.kamel.samples"

    defaultConfig {
        minSdk = 28
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
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
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
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "Metal",
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "CoreText",
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "CoreGraphics"
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
        commonMain {
            dependencies {
                implementation(projects.kamelImageDefault)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.okio)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }

        androidMain {
            dependencies {
                implementation(projects.kamelFetcher.kamelFetcherResourcesAndroid)
                implementation(projects.kamelMapper.kamelMapperResourcesIdAndroid)
                implementation(projects.kamelDecoder.kamelDecoderImageBitmapResizing)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.google.android.material)
                implementation(libs.slf4j)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(projects.kamelDecoder.kamelDecoderSvgBatik)
                implementation(projects.kamelFetcher.kamelFetcherResourcesJvm)
                implementation(compose.desktop.currentOs)
                implementation(libs.slf4j)
            }
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

compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosArm64"))
    distributions {
        targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg)
        packageName = "Native-Sample"
        packageVersion = "1.0.0"
    }
}