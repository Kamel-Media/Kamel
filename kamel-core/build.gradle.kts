import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    multiplatform
    compose
    `maven-publish`
    signing
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    jvm()
    js(IR) {
        browser()
    }
    for (target in Targets.macosTargets) {
        targets.add(
            (presets.getByName(target).createTarget(target) as KotlinNativeTarget).apply {
                binaries.forEach {
                    it.apply {
                        freeCompilerArgs += listOf(
                            "-linker-option", "-framework", "-linker-option", "Metal"
                        )
                    }
                }
            }
        )
    }
    for (target in Targets.iosTargets) {
        targets.add(
            (presets.getByName(target).createTarget(target) as KotlinNativeTarget).apply {
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
        )
    }



    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.runtime)
                api(Dependencies.Ktor.Core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":kamel-tests"))
                implementation(kotlin("test"))
                implementation(Dependencies.Testing.Ktor)
                implementation(Dependencies.Coroutines.Test)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.KotlinReflect)
            }
        }

        val jvmTest by getting {
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

        val darwinMain by creating {
            dependsOn(nonJvmMain)
        }

        val darwinTest by creating {
            dependsOn(nonJvmTest)
        }

        Targets.darwinTargets.forEach { target ->
            getByName("${target}Main") {
                dependsOn(darwinMain)
            }
            getByName("${target}Test") {
                dependsOn(darwinTest)
            }
        }

        all {
            languageSettings.apply {
                optIn("kotlin.Experimental")
            }
        }

    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val dependsOnTasks = mutableListOf<String>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOnTasks.add(this.name.replace("publish", "sign").replaceAfter("Publication", ""))
    dependsOn(dependsOnTasks)
}