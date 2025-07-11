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

    namespace = "io.kamel.decoder.svg.std"

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
        publishLibraryVariants("release", "debug")
    }
    jvm()
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

        commonMain {
            dependencies {
                implementation(projects.kamelCore)
//                // todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
                implementation(compose.ui)
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
