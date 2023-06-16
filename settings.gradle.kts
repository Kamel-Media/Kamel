pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.51.0"
    }
}

plugins {
    id("de.fayard.refreshVersions")
}


refreshVersions { // Optional: configure the plugin
    // ...
}

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image", "kamel-tests")
