pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}
enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image")