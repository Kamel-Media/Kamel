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
        namespace = "io.kamel.decoder.image.vector"
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
                api(projects.kamelCore)
                implementation(libs.compose.ui)
                // todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
            }
        }

        val nonJvmAndAndroidMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.pdvrieze.xmlutil.serialization)
            }
        }

        androidMain {
            dependsOn(nonJvmAndAndroidMain)
        }

        jsMain {
            dependsOn(nonJvmAndAndroidMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonJvmAndAndroidMain)
        }

        appleMain {
            dependsOn(nonJvmAndAndroidMain)
        }

    }
}
