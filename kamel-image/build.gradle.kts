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
    jvm("desktopJvm")
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

        val commonJvmMain by creating {
            dependsOn(commonMain)
        }

        val commonJvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.jetbrains.compose.ui.ui.test.junit4)
            }
        }

        val desktopJvmTest by getting {
            dependsOn(commonJvmTest)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val desktopJvmMain by getting {
            dependsOn(commonJvmMain)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        androidMain.get().dependsOn(commonJvmMain)

        val nonJvmMain by creating {
            dependsOn(commonMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonJvmMain)
        }
        jsMain.get().dependsOn(nonJvmMain)
        nativeMain.get().dependsOn(nonJvmMain)
    }
}
