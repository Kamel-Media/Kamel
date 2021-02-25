plugins {
    multiplatform
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Testing.Ktor)
                implementation(Dependencies.Testing.Compose)
                implementation(Dependencies.Testing.Coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}