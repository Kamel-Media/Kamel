import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.com.android.kotlin.multiplatform.library)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

kotlin {
    explicitApi = ExplicitApiMode.Warning
    android {
        namespace = "io.kamel.fetcher.resources.android"
        compileSdk = 36
        minSdk = 21
    }
    applyDefaultHierarchyTemplate()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(projects.kamelCore)
                // todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
            }
        }
    }
}
