plugins {
    id("org.jetbrains.dokka") version "1.4.20"
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        maven(url = "https://kotlin.bintray.com/ktor")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:0.3.0-build148")
        classpath("com.android.tools.build:gradle:7.0.0-alpha05")
        classpath(kotlin("gradle-plugin", version = "1.4.21-2"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        maven(url = "https://kotlin.bintray.com/ktor")
    }

    ext {
        set("GroupId", "com.alialbaali.kamel")
        set("Version", "0.0.5")
    }

    group = ext["GroupId"] as String
    version = ext["Version"] as String
}
