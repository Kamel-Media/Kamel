import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.application)
}

android {
    compileSdk = 36
    namespace = "io.kamel.samples"

    defaultConfig {
        minSdk = 28
        targetSdk = 36
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
        useEsModules()
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useEsModules()
        browser()
        binaries.executable()
    }
    fun iosTargets(config: KotlinNativeTarget.() -> Unit) {
        iosArm64(config)
        iosSimulatorArm64(config)
        iosX64(config)
    }
    iosTargets {
        binaries.framework {
            baseName = "shared"
            isStatic = true
        }
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
        binaries.executable()
    }
    applyDefaultHierarchyTemplate()

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

dependencies {
    // https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio#focus=Comments-27-11400795.0-0
    debugImplementation(libs.androidx.ui.tooling)
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