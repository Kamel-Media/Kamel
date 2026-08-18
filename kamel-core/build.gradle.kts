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
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
    explicitApi = ExplicitApiMode.Warning

    android {
        namespace = "io.kamel.core.cache"
        compileSdk = 36
        minSdk = 21
        withHostTest {}
    }
    jvm("desktop")
    js {
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
    macosArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain {
            dependencies {
                implementation(libs.compose.foundation)
                implementation(libs.ktor.client.core)
                implementation(libs.okio)
                implementation(libs.cache4k)
                // todo: remove this https://youtrack.jetbrains.com/issue/CMP-4442
                implementation(libs.compose.components.resources)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.okio.fakefilesystem)
                implementation(libs.compose.components.resources)
            }
        }

        val commonJvmMain = create("commonJvmMain") {
            dependsOn(commonMain.get())
        }

        val commonJvmTest = create("commonJvmTest") {
            dependsOn(getByName("commonTest"))
        }

        getByName("desktopMain") {
            dependsOn(commonJvmMain)
        }

        getByName("desktopTest") {
            dependsOn(commonJvmTest)
        }

        androidMain {
            dependsOn(commonJvmMain)
            dependencies {
                implementation(libs.androidx.startup)
            }
        }

        getByName("androidHostTest") {
            dependsOn(commonJvmTest)
        }

        create("nonJvmMain") {
            dependsOn(commonMain.get())
        }

        jsMain {
            dependsOn(get("nonJvmMain"))
        }

        wasmJsMain {
            dependsOn(get("nonJvmMain"))
        }

        appleMain {
            dependsOn(get("nonJvmMain"))
        }

    }
}
