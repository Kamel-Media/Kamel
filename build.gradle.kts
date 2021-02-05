plugins {
    id("org.jetbrains.dokka") version "1.4.20"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven(url = "https://dl.bintray.com/kotlin/dokka")
    }

    ext {
        set("GroupId", "com.alialbaali.kamel")
        set("Version", "0.0.5")
    }

    group = ext["GroupId"] as String
    version = ext["Version"] as String
}
