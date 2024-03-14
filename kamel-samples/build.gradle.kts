import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
        applyBinaryen {
            binaryenArgs = mutableListOf(
                "--enable-nontrapping-float-to-int",
                "--enable-gc",
                "--enable-reference-types",
                "--enable-exception-handling",
                "--enable-bulk-memory",
                "--inline-functions-with-loops",
                "--traps-never-happen",
                "--fast-math",
                "--closed-world",
                "--metrics",
                "-O3", "--gufa", "--metrics",
                "-O3", "--gufa", "--metrics",
                "-O3", "--gufa", "--metrics",
            )
        }
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
                implementation(projects.kamelImage)
                implementation(projects.kamelImageDefault)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.okio)
                implementation(compose.components.resources)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(projects.kamelFetcher.kamelFetcherResourcesAndroid)
                implementation(projects.kamelMapper.kamelMapperResourcesIdAndroid)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.google.android.material)
                implementation(libs.slf4j)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(projects.kamelDecoder.kamelDecoderSvgBatik)
                implementation(projects.kamelFetcher.kamelFetcherResourcesJvm)
                implementation(compose.desktop.currentOs)
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

compose {
    kotlinCompilerPlugin.set("1.5.10")
}