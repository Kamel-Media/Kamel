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
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

    for (target in Targets.nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":kamel-core"))
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

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(compose.material)
                implementation(Dependencies.Testing.Compose)
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
            dependencies {
                implementation(Dependencies.Ktor.CIO)
                implementation(compose.desktop.currentOs)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies{
                //todo: remove below when resolved https://github.com/JetBrains/compose-jb/issues/2238
                // the app will build without these imports. But Intellij highlights the compose
                // imports in red if we do not add them here. (this is with the android 7.2
                // gradle plugin, 7.1 works correctly but causes lint errors when building)
                implementation(Dependencies.Android.Annotation)
                implementation(Dependencies.Android.UIGraphics)
            }
        }

        val androidTest by getting {
            dependsOn(jvmTest)
        }

        val nonJvmMain by creating {
            dependsOn(commonMain)
        }

        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        val darwinMain by creating {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(Dependencies.Ktor.Darwin)
            }
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
