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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    "kamel-samples",
    "kamel-core",
    "kamel-image",
    "kamel-image-default",
    "kamel-decoder:kamel-decoder-image-bitmap",
    "kamel-decoder:kamel-decoder-image-vector",
    "kamel-decoder:kamel-decoder-animated-image",
    "kamel-decoder:kamel-decoder-svg-batik",
    "kamel-decoder:kamel-decoder-svg-std",
    "kamel-fetcher:kamel-fetcher-resources-jvm",
    "kamel-fetcher:kamel-fetcher-resources-android",
    "kamel-mapper:kamel-mapper-resources-id-android"
)
