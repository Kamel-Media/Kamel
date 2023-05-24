import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import java.io.FileFilter

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
                implementation(Dependencies.Coroutines.Core)
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


// todo: Remove when resolved: https://github.com/icerockdev/moko-resources/issues/372
tasks.withType<KotlinNativeLink>()
    .matching { linkTask ->
        linkTask.binary is AbstractExecutable
    }
    .configureEach {
        val task: KotlinNativeLink = this

        this.doLast {
            val binary: NativeBinary = task.binary
            val outputDir: File = task.outputFile.get().parentFile
            task.libraries
                .filter { library -> library.extension == "klib" }
                .filter(File::exists)
                .forEach { inputFile ->
                    val klibKonan = org.jetbrains.kotlin.konan.file.File(inputFile.path)
                    val klib = KotlinLibraryLayoutImpl(
                        klib = klibKonan,
                        component = "default"
                    )
                    val layout = klib.extractingToTemp

                    // extracting bundles
                    layout
                        .resourcesDir
                        .absolutePath
                        .let(::File)
                        .listFiles(FileFilter { it.extension == "bundle" })
                        // copying bundles to app
                        ?.forEach { bundleFile ->
                            logger.info("${bundleFile.absolutePath} copying to $outputDir")
                            bundleFile.copyRecursively(
                                target = File(outputDir, bundleFile.name),
                                overwrite = true
                            )
                        }
                }
        }
    }


// todo: remove after https://github.com/icerockdev/moko-resources/issues/392 resolved
// copy resources from kamel-tests into the proper directory for kamel-samples so they are packaged for
// the web app
tasks.register<Copy>("jsCopyResourcesFromKamelTests") {
    from("../kamel-tests/build/generated/moko/jsMain/iokameltests/res")
    into("build/generated/moko/jsMain/iokamelcore/res")
    dependsOn(":kamel-tests:generateMRjsMain")
}
tasks.getByName("jsProcessResources").dependsOn("jsCopyResourcesFromKamelTests")

apply(from = "$rootDir/gradle/pack-core-tests-resources.gradle.kts")