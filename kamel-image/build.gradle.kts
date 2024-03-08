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
    jvm("desktop")
    js(IR) {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
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
                api(projects.kamelCore)
                implementation(compose.foundation)
                implementation(libs.ktor.client.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.jetbrains.compose.ui.ui.test.junit4)
            }
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

    }
}
