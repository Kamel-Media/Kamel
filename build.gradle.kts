import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.compose") version "0.3.0-build141"
    `maven-publish`
}

group = "io.kamel"
version = "0.0.2"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    val ktor_version = "1.5.0"
    api("io.ktor:ktor-client-core:$ktor_version")
    api("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
}

kotlin {
    explicitApi = ExplicitApiMode.Warning
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.kamel"
            artifactId = "kamel-core"
            version = "0.0.2"

            from(components["kotlin"])
        }
    }
}