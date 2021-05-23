import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    multiplatform
    compose
    `android-library`
    `maven-publish`
    signing
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
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

    android {
        publishAllLibraryVariants()
    }
    jvm("desktop")

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":kamel-core"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":kamel-tests"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Dependencies.Testing.Ktor)
                implementation(Dependencies.Testing.Compose)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.CIO)
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(kotlin("test-junit"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.Android)
                implementation(Dependencies.Android.Appcompat)
                implementation(Dependencies.Android.Core)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("androidx.test:core:1.3.0")
                implementation("androidx.test.ext:junit:1.1.2")
            }
        }

        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
            }
        }

        targets.all {
            compilations.all {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
                }
            }
        }

    }
}
