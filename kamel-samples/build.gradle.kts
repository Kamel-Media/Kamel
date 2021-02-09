import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                useIR = true
                jvmTarget = "11"
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":kamel-image"))
                implementation(compose.material)
                implementation(compose.animation)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.3.2")
                implementation("androidx.appcompat:appcompat:1.2.0")
                implementation("com.google.android.material:material:1.2.1")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
                enableLanguageFeature(LanguageFeature.AllowResultInReturnType.toString())
            }
        }

        targets.all {
            compilations.all {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
                }
            }
        }

    }
}

compose {
    desktop {
        application {
            mainClass = "io.kamel.samples.MainKt"
        }
    }
}