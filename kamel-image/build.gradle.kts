import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.com.android.library)
    `maven-publish`
    signing
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        multiDexEnabled = true
    }

    namespace = "io.kamel.image"

    // https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

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
    for (target in Targets.nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":kamel-core"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(libs.ktor.client.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":kamel-tests"))
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(compose.material)
                implementation(libs.jetbrains.compose.ui.ui.test.junit4)
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
            dependencies {
                implementation(libs.apache.batik.transcoder)
            }
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(compose.desktop.currentOs)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                implementation(libs.com.caverok.androidsvg)
                implementation(libs.pdvrieze.xmlutil.serialization)
            }
        }

        val androidUnitTest by getting {
            dependsOn(jvmTest)
        }

        val nonJvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.pdvrieze.xmlutil.serialization)
            }
        }

        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        val jsMain by getting {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        val darwinMain by creating {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
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

    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val dependsOnTasks = mutableListOf<String>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOnTasks.add(this.name.replace("publish", "sign").replaceAfter("Publication", ""))
    dependsOn(dependsOnTasks)
}

