import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21-2"
    `maven-publish`
}

group = "io.kamel"
version = "0.0.3"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            useIR = true
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.kamel"
            artifactId = "kamel-core"
            version = "0.0.3"

            from(components["kotlin"])
        }
    }
}