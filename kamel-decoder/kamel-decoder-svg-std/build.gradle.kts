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
        namespace = "io.kamel.decoder.svg.std"
        compileSdk = 36
        minSdk = 21
    }
    jvm()
    js(IR) {
        useEsModules()
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useEsModules()
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosArm64()
    applyDefaultHierarchyTemplate()
    sourceSets {

        commonMain {
            dependencies {
                implementation(projects.kamelCore)
//                // todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
                implementation(libs.compose.ui)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.com.caverok.androidsvg)
            }
        }

        val nonJvmMain by creating {
            dependsOn(commonMain.get())
        }

        jsMain {
            dependsOn(nonJvmMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonJvmMain)
        }

        appleMain {
            dependsOn(nonJvmMain)
        }
    }
}
