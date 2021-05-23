pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

rootProject.name = "Kamel"

include("kamel-samples", "kamel-core", "kamel-image", "kamel-tests")
