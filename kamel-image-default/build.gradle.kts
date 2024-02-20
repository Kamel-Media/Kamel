import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

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
                implementation(compose.foundation)
            }
        }

        jvmMain {
            dependencies {
                api(projects.kamelFetcher.kamelFetcherResourcesJvm)
                implementation(libs.ktor.client.cio)
            }
        }

        androidMain {
            dependencies {
                api(projects.kamelFetcher.kamelFetcherResourcesAndroid)
                api(projects.kamelMapper.kamelMapperResourcesIdAndroid)
                implementation(libs.ktor.client.android)
            }
        }

        jsMain {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        appleMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

    }
}
