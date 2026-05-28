import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.kotlin.multiplatform.library)
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    android {
        namespace = "io.kamel.samples"
        compileSdk = 36
        minSdk = 28
        packaging {
            resources {
                excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
            }
        }
    }
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
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.okio)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
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
                // Compose @Preview tooling in androidMain (samples module isn't published, so size is fine).
                // https://youtrack.jetbrains.com/issue/KTIJ-32720
                implementation(libs.androidx.ui.tooling)
            }
        }

        val desktopMain by getting {
            resources.srcDir("src/commonMain/composeResources")
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
    resources {
        packageOfResClass = "io.kamel.samples.generated.resources"
    }
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
