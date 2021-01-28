import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21-2"
}

group = "io.kamel"
version = "0.0.4"

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