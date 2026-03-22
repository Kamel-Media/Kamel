import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.kotlin.multiplatform.library)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

kotlin {
    explicitApi = ExplicitApiMode.Warning

    android {
        namespace = "io.kamel.image.defaults"
        compileSdk = 36
        minSdk = 21
    }
    jvm()
    js(IR) {
        useEsModules()
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        useEsModules()
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosArm64()
    applyDefaultHierarchyTemplate()

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(projects.kamelImage)
                api(projects.kamelDecoder.kamelDecoderSvgStd)
                api(projects.kamelDecoder.kamelDecoderImageBitmap)
                api(projects.kamelDecoder.kamelDecoderImageVector)
                api(projects.kamelDecoder.kamelDecoderAnimatedImage)
                implementation(libs.compose.foundation)
            }
        }

        val commonJvmMain by creating {
            dependsOn(commonMain)
        }

        jvmMain {
            dependsOn(commonJvmMain)
            dependencies {
                api(projects.kamelFetcher.kamelFetcherResourcesJvm)
                implementation(libs.ktor.client.cio)
            }
        }

        androidMain {
            resources.srcDirs("src/commonJvmMain/resources")
            dependsOn(commonJvmMain)
            dependencies {
                api(projects.kamelFetcher.kamelFetcherResourcesAndroid)
                api(projects.kamelMapper.kamelMapperResourcesIdAndroid)
                implementation(libs.ktor.client.android)
            }
        }

        val nonJvmMain by creating {
            dependsOn(commonMain)
        }

        jsMain {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        val wasmJsMain by getting {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        nativeMain {
            dependsOn(nonJvmMain)
        }

        appleMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

    }
}
