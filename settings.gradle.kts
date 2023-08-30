import de.fayard.refreshVersions.core.StabilityLevel

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.60.2"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("de.fayard.refreshVersions")
}


refreshVersions {
//    rejectVersionIf {
//        candidate.stabilityLevel != StabilityLevel.Stable
//    }
}

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image", "kamel-tests")
