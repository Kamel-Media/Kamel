import org.jetbrains.compose.compose
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    `maven-publish`
    signing
}

kotlin {

    explicitApi = ExplicitApiMode.Warning

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
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
                api(compose("org.jetbrains.compose.ui:ui-test-junit4"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-reflect:1.4.30")
            }
        }

        val jvmTest by getting {
            dependencies {
                api(kotlin("test-junit"))
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
