plugins {
    multiplatform
    mokoResources
}

kotlin {
    jvm()
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

multiplatformResources {
    multiplatformResourcesPackage = "io.kamel.tests"
}

tasks.findByName("jvmProcessResources")!!.dependsOn("generateMRcommonMain")
tasks.findByName("jvmProcessResources")!!.dependsOn("generateMRjvmMain")