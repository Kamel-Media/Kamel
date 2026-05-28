plugins {
    alias(libs.plugins.com.android.application)
}

android {
    namespace = "io.kamel.samples.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.kamel.samples"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }
}

dependencies {
    implementation(projects.kamelSamples.shared)
}
