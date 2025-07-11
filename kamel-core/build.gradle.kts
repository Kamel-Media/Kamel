import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
    explicitApi = ExplicitApiMode.Warning

    androidTarget {
        publishLibraryVariants("release", "debug")
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
        all {
            languageSettings.apply {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.okio)
                implementation(libs.cache4k)
                // todo: remove this https://youtrack.jetbrains.com/issue/CMP-4442
                implementation(compose.components.resources)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.okio.fakefilesystem)
                implementation(compose.components.resources)
            }
        }

        val commonJvmMain = create("commonJvmMain") {
            dependsOn(commonMain)
        }

        val commonJvmTest = create("commonJvmTest") {
            dependsOn(commonTest)
        }

        val desktopMain by getting {
            dependsOn(commonJvmMain)
        }

        val desktopTest by getting {
            dependsOn(commonJvmTest)
        }

        val androidMain by getting {
            dependsOn(commonJvmMain)
            dependencies {
                implementation(libs.androidx.startup)
            }
        }

        val androidUnitTest by getting {
            dependsOn(commonJvmTest)
        }

        val nonJvmMain by creating {
            dependsOn(commonMain)
        }

        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        val jsMain by getting {
            dependsOn(nonJvmMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonJvmMain)
        }

        val appleMain by getting {
            dependsOn(nonJvmMain)
        }

        val appleTest by getting {
            dependsOn(nonJvmTest)
        }

    }
}

android {
    namespace = "io.kamel.core.cache"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }
}
