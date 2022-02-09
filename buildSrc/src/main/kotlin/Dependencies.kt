object Dependencies {

    const val ComposeGradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:${Versions.Compose}"
    const val KotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
    const val KotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"

    object Android {
        const val Appcompat = "androidx.appcompat:appcompat:${Versions.Android.Appcompat}"
        const val Core = "androidx.core:core-ktx:${Versions.Android.Core}"
        const val ActivityCompose = "androidx.activity:activity-compose:${Versions.Android.ActivityCompose}"
        const val GradlePlugin = "com.android.tools.build:gradle:${Versions.AGP}"
        const val Material = "com.google.android.material:material:${Versions.Android.Material}"
    }

    object Ktor {
        const val Core = "io.ktor:ktor-client-core:${Versions.Ktor}"
        const val Logging = "io.ktor:ktor-client-logging:${Versions.Ktor}"
        const val Android = "io.ktor:ktor-client-android:${Versions.Ktor}"
        const val CIO = "io.ktor:ktor-client-cio:${Versions.Ktor}"
    }

    object Testing {
        const val Ktor = "io.ktor:ktor-client-mock:${Versions.Ktor}"
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Coroutines}"
        const val Compose = "org.jetbrains.compose.ui:ui-test-junit4:${Versions.Compose}"
    }
    
}

private object Versions {

    const val Kotlin = "1.6.10"
    const val Ktor = "2.0.0-beta-1"
    const val Coroutines = "1.6.0"
    const val Compose = "1.0.1"
    const val AGP = "7.0.4"

    object Android {
        const val Appcompat = "1.4.1"
        const val Core = "1.6.0"
        const val ActivityCompose = "1.4.0"
        const val Material = "1.5.0"
    }

}