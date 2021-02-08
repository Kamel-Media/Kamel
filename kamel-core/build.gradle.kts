import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

ext {
    set("ArtifactId", "kamel-core")
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
    jvm("desktop")
    jvm()

    sourceSets {

        val ktorVersion = "1.5.1"

        val commonMain by getting {
            dependencies {
                api(compose.ui)
                api(compose.foundation)
                api(compose.runtime)
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
                api("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-reflect:1.4.21-2")
            }
        }

        val jvmTest by getting {
            dependencies {
                api(kotlin("test-junit"))
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api("io.ktor:ktor-client-android:$ktorVersion")
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.2")
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