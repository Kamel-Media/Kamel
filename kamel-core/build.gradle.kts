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
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-reflect:1.4.21-2")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
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
