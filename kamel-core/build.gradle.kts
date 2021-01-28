import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "0.3.0-build143"
    `maven-publish`
}

repositories {
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
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.kamel"
            artifactId = "kamel-core"
            version = "0.0.4"

            from(components["kotlin"])
        }
    }
}