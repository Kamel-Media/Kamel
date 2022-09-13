import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.tasks.AbstractNativeMacApplicationPackageAppDirTask
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import org.jetbrains.kotlin.konan.file.File as KonanFile
import java.io.FileFilter

plugins {
    multiplatform
    compose
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

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }

    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
    }

    android()
    jvm("desktop")

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


    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":kamel-image"))
                implementation(project(":kamel-tests"))
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

        val macosMain by creating {
            dependsOn(commonMain)
        }

        Targets.macosTargets.forEach { target ->
            getByName("${target}Main") {
                dependsOn(macosMain)
            }
        }

        all {
            languageSettings.apply {
                optIn("kotlin.Experimental")
            }
        }

    }
}

compose {
    desktop {
        application {
            mainClass = "io.kamel.samples.DesktopSampleKt"
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
