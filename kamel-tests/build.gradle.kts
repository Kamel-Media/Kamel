plugins {
    `android-library`
    multiplatform
    mokoResources
}

kotlin {
    jvm()
    android()
    js(IR) {
        browser()
    }
    for (target in Targets.nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Testing.Ktor)
                implementation(Dependencies.Coroutines.Core)
                api(Dependencies.MokoResources.Core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                api(Dependencies.MokoResources.Test)
            }
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

    //https://github.com/icerockdev/moko-resources/issues/403
    sourceSets["main"].res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
}


multiplatformResources {
    multiplatformResourcesPackage = "io.kamel.tests"
}