plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.dev.icerock.mobile.multiplatform.resources)
}

kotlin {
    jvm()
    androidTarget()
    js(IR) {
        browser()
    }
    for (target in Targets.nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.coroutines.core)
                api(libs.dev.icerock.moko.resources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.dev.icerock.moko.resources.test)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependsOn(commonMain)
            resources.srcDirs("build/generated/moko/jsMain/src")
        }

        val darwinMain by creating {
            dependsOn(commonMain)
        }

        val darwinTest by creating {
            dependsOn(commonTest)
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

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 16
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}


multiplatformResources {
    multiplatformResourcesPackage = "io.kamel.tests"
}