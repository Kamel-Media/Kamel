import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform") version "1.4.21-2"
    id("org.jetbrains.compose") version "0.3.0-build148"
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

kotlin {
    explicitApi = ExplicitApiMode.Warning
    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                useIR = true
                jvmTarget = "11"
            }
        }
    }

    val ktorVersion = "1.5.1"

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.runtime)
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
        val commonTest by getting

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-apache:$ktorVersion")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
            }
        }

        val desktopTest by getting

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

ext {
    set("ArtifactId", "kamel-core")
}
