import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

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
        namespace = "io.kamel.image"
        compileSdk = 36
        minSdk = 21
    }
    jvm("desktopJvm")
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
                api(projects.kamelCore)
                implementation(libs.compose.foundation)
                implementation(libs.ktor.client.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.compose.material3)
                implementation(kotlin("test"))
                implementation(libs.compose.ui.test)
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
