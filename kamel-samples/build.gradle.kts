import org.jetbrains.compose.desktop.application.tasks.AbstractNativeMacApplicationPackageAppDirTask
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import java.io.File
import java.io.FileFilter
import org.jetbrains.compose.experimental.dsl.IOSDevices as IOSDevices1
import org.jetbrains.kotlin.konan.file.File as KonanFile

plugins {
    multiplatform
    compose
    mokoResources
    `android-application`
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        resources {
            excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
    }

    android()
    jvm("desktop")
    js(IR) {
        browser()
        binaries.executable()
    }
    for (target in Targets.macosTargets) {
        targets.add(
            (presets.getByName(target)
                .createTarget(target) as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget).apply {
                binaries.executable {
                    freeCompilerArgs += listOf(
                        "-linker-option", "-framework", "-linker-option", "Metal"
                    )
                }
            }
        )
    }
    for (target in Targets.iosTargets) {
        targets.add(
            (presets.getByName(target)
                .createTarget(target.replace("ios", "uikit"))
                    as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget).apply {
                binaries.executable {
                    entryPoint = "main"
                    freeCompilerArgs += listOf(
                        "-linker-option", "-framework", "-linker-option", "Metal",
                        "-linker-option", "-framework", "-linker-option", "CoreText",
                        "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                    )
                }
            }
        )
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":kamel-image"))
                implementation(project(":kamel-tests"))
                implementation(compose.foundation)
                implementation(compose.material)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.Android.Appcompat)
                implementation(Dependencies.Android.ActivityCompose)
                implementation(Dependencies.Android.Material)
                implementation(Dependencies.Ktor.Android)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(Dependencies.Ktor.CIO)
            }
        }

        val darwinMain by creating {
            dependsOn(commonMain)
        }

        val macosMain by creating {
            dependsOn(darwinMain)
        }

        Targets.macosTargets.forEach { target ->
            getByName("${target}Main") {
                dependsOn(macosMain)
            }
        }

        val uikitMain by creating {
            dependsOn(darwinMain)
        }

        Targets.iosTargets.map { target ->
            target.replace("ios", "uikit")
        }.forEach { target ->
            getByName("${target}Main") {
                dependsOn(uikitMain)
            }
        }

        all {
            languageSettings.apply {
                optIn("kotlin.Experimental")
            }
        }

    }
}


multiplatformResources {
    multiplatformResourcesPackage = "io.kamel.samples"
}


compose {
    desktop {
        application {
            mainClass = "io.kamel.samples.DesktopSampleKt"
        }
    }
}

compose.experimental {
    web.application {}
    uikit.application {
        bundleIdPrefix = "io.kamel.samples"
        projectName = "kamel samples"
        deployConfigurations {
            simulator("IPhone13") {
                //Usage: ./gradlew iosDeployIPhone8Debug
                device = IOSDevices1.IPHONE_13
            }
            simulator("IPad") {
                //Usage: ./gradlew iosDeployIPadDebug
                device = IOSDevices1.IPAD_PRO_11_INCH_3rd_Gen
            }
            connectedDevice("Device") {
                //First need specify your teamId here, or in local.properties (compose.ios.teamId=***)
                //teamId="***"
                //Usage: ./gradlew iosDeployDeviceRelease
            }
        }
    }
}


compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosArm64"))
    distributions {
        targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg)
        packageName = "Native-Sample"
        packageVersion = "1.0.0"
    }
}

// TODO: remove when https://youtrack.jetbrains.com/issue/KT-50778 fixed
project.tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile::class.java).configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xir-dce-runtime-diagnostic=log"
    )
}

// todo: remove after https://github.com/icerockdev/moko-resources/issues/392 resolved
// copy resources from kamel-tests into the proper directory for kamel-samples so they are packaged for
// the web app
tasks.register<Copy>("jsCopyResourcesFromKamelTests") {
    from("../kamel-tests/build/generated/moko/jsMain/iokameltests/res")
    into("build/generated/moko/jsMain/iokamelsamples/res")
    dependsOn(":kamel-tests:generateMRjsMain")
}
tasks.getByName("jsProcessResources").dependsOn("jsCopyResourcesFromKamelTests")

tasks.register<Copy>("desktopCopyResourcesFromKamelTests") {
    from("../kamel-tests/build/generated/moko/jvmMain/iokameltests/res")
    into("build/generated/moko/desktopMain/iokamelsamples/res")
    dependsOn(":kamel-tests:generateMRjvmMain")
}
tasks.getByName("desktopProcessResources").dependsOn("desktopCopyResourcesFromKamelTests")

// todo: Remove when resolved: https://github.com/icerockdev/moko-resources/issues/372
tasks.withType<KotlinNativeLink>()
    .matching { linkTask -> linkTask.binary is AbstractExecutable }
    .configureEach {
        val task: KotlinNativeLink = this

        doLast {
            val binary: NativeBinary = task.binary
            val outputDir: File = task.outputFile.get().parentFile
            task.libraries
                .filter { library -> library.extension == "klib" }
                .filter(File::exists)
                .forEach { inputFile ->
                    val klibKonan = KonanFile(inputFile.path)
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

tasks.withType<AbstractNativeMacApplicationPackageAppDirTask> {
    val task: AbstractNativeMacApplicationPackageAppDirTask = this

    doLast {
        val execFile: File = task.executable.get().asFile
        val execDir: File = execFile.parentFile
        val destDir: File = task.destinationDir.asFile.get()
        val bundleID: String = task.bundleID.get()

        val outputDir = File(destDir, "$bundleID.app/Contents/Resources")
        outputDir.mkdirs()

        execDir.listFiles().orEmpty()
            .filter { it.extension == "bundle" }
            .forEach { bundleFile ->
                logger.info("${bundleFile.absolutePath} copying to $outputDir")
                bundleFile.copyRecursively(
                    target = File(outputDir, bundleFile.name),
                    overwrite = true
                )
            }
    }
}
