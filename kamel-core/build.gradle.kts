import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import java.io.FileFilter

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    `maven-publish`
    signing
    alias(libs.plugins.com.android.library)
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

    fun iosTargets(config: KotlinNativeTarget.() -> Unit) {
        iosArm64(config)
        iosSimulatorArm64(config)
        iosX64(config)
    }
    iosTargets {
        binaries.forEach {
            it.apply {
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    fun macosTargets(config: KotlinNativeTarget.() -> Unit) {
        macosX64(config)
        macosArm64(config)
    }
    macosTargets {
        binaries.executable {
            freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal"
            )
            linkerOpts.add("-lsqlite3")
        }
    }
    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.okio)
                implementation(libs.cache4k)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.okio.fakefilesystem)
                implementation(libs.compose.components.resources)
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

        val appleMain by getting {
            dependsOn(nonJvmMain)
        }

        val appleTest by getting {
            dependsOn(nonJvmTest)
        }

    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val dependsOnTasks = mutableListOf<String>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOnTasks.add(this.name.replace("publish", "sign").replaceAfter("Publication", ""))
    dependsOn(dependsOnTasks)
}

android {
    namespace = "io.kamel.core.cache"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
}
