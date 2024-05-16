import de.fayard.refreshVersions.core.StabilityLevel

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.60.5"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}

plugins {
    id("de.fayard.refreshVersions")
}


refreshVersions {
    rejectVersionIf {
        @Suppress("UnstableApiUsage")
        candidate.stabilityLevel >= StabilityLevel.Beta
    }
}

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image")
