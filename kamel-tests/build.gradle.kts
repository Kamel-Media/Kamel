plugins {
    multiplatform
    mokoResources
}

kotlin {
    jvm()
    for (target in Targets.nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Testing.Ktor)
                implementation(Dependencies.Testing.Coroutines)
                implementation(Dependencies.Testing.MokoResources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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