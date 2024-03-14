import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        multiDexEnabled = true
    }

    namespace = "io.kamel.image"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

kotlin {
    explicitApi = ExplicitApiMode.Warning

    androidTarget {
        publishAllLibraryVariants()
    }
    jvm()
    js(IR) {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosX64()
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
                implementation(compose.foundation)
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
