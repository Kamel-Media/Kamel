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
        withHostTest {}
    }
    jvm("desktopJvm")
    js {
        useEsModules()
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        useEsModules()
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    applyDefaultHierarchyTemplate()

    sourceSets {

        commonMain {
            dependencies {
                api(projects.kamelCore)
                implementation(libs.compose.foundation)
                implementation(libs.ktor.client.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.compose.material3)
                implementation(kotlin("test"))
                implementation(libs.compose.ui.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        create("commonJvmMain") {
            dependsOn(commonMain.get())
        }

        getByName("desktopJvmTest") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        getByName("desktopJvmMain") {
            dependsOn(get("commonJvmMain"))
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        androidMain {
            dependsOn(get("commonJvmMain"))
        }

        create("nonJvmMain") {
            dependsOn(commonMain.get())
        }

        wasmJsMain {
            dependsOn(get("nonJvmMain"))
        }
        jsMain {
            dependsOn(get("nonJvmMain"))
        }
        nativeMain {
            dependsOn(get("nonJvmMain"))
        }
    }
}
