import de.fayard.refreshVersions.core.StabilityLevel

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.60.3"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

plugins {
    id("de.fayard.refreshVersions")
}


refreshVersions {
//    rejectVersionIf {
//        @Suppress("UnstableApiUsage")
//        candidate.stabilityLevel != StabilityLevel.Stable
//    }
}

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image")
