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
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.dev.icerock.mobile.multiplatform.resources)
    kotlin("native.cocoapods")
}

android {
    compileSdk = 34
    namespace = "io.kamel.samples"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    androidTarget()
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
                .createTarget(target)
                    as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget).apply {
                binaries.framework {
                    freeCompilerArgs += listOf(
                        "-linker-option", "-framework", "-linker-option", "Metal",
                        "-linker-option", "-framework", "-linker-option", "CoreText",
                        "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                    )
                }
            }
        )
    }

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/Kamel-Media/Kamel"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../kamel-sample-ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
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
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.google.android.material)
                implementation(libs.ktor.client.android)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.cio)
            }
        }

        val jsMain by getting {
            dependsOn(commonMain)
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

        val iosMain by creating {
            dependsOn(darwinMain)
        }
        val iosTest by creating {
            dependsOn(darwinMain)
        }
        Targets.iosTargets.forEach { target ->
            getByName("${target}Main") {
                dependsOn(iosMain)
            }
        }
        Targets.iosTargets.forEach { target ->
            getByName("${target}Test") {
                dependsOn(iosTest)
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
}


compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosArm64"))
    distributions {
        targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg)
        packageName = "Native-Sample"
        packageVersion = "1.0.0"
    }
}


//// todo: remove after https://github.com/icerockdev/moko-resources/issues/392 resolved
//// copy resources from kamel-tests into the proper directory for kamel-samples so they are packaged for
//// the web app
tasks.register<Copy>("jsCopyResourcesFromKamelTests") {
    from("../kamel-tests/build/generated/moko/jsMain/iokameltests/res")
    into("build/generated/moko/jsMain/iokamelsamples/res")
    dependsOn(":kamel-tests:generateMRjsMain")
}
tasks.getByName("jsProcessResources").dependsOn("jsCopyResourcesFromKamelTests")
//
//tasks.register<Copy>("desktopCopyResourcesFromKamelTests") {
//    from("../kamel-tests/build/generated/moko/jvmMain/iokameltests/res")
//    into("build/generated/moko/desktopMain/iokamelsamples/res")
//    dependsOn(":kamel-tests:generateMRjvmMain")
//}
//tasks.getByName("desktopProcessResources").dependsOn("desktopCopyResourcesFromKamelTests")

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
