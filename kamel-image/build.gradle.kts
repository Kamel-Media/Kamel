import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

android {
    compileSdk = 36

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
                implementation(compose.material3)
                implementation(kotlin("test"))
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val commonJvmMain by creating {
            dependsOn(commonMain)
        }

        val desktopJvmTest by getting {
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
