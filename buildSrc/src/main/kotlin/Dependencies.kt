object Dependencies {

    const val ComposeGradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:${Versions.Compose}"
    const val KotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
    const val KotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"

    object Android {
        const val ActivityCompose = "androidx.activity:activity-compose:${Versions.Android.ActivityCompose}"
        const val Appcompat = "androidx.appcompat:appcompat:${Versions.Android.Appcompat}"
        const val GradlePlugin = "com.android.tools.build:gradle:${Versions.AGP}"
        const val Material = "com.google.android.material:material:${Versions.Android.Material}"
        const val Annotation = "androidx.annotation:annotation:${Versions.Android.Annotation}"
    }

    object Ktor {
        const val Core = "io.ktor:ktor-client-core:${Versions.Ktor}"
        const val Android = "io.ktor:ktor-client-android:${Versions.Ktor}"
        const val Darwin = "io.ktor:ktor-client-darwin:${Versions.Ktor}"
        const val Js = "io.ktor:ktor-client-js:${Versions.Ktor}"
        const val CIO = "io.ktor:ktor-client-cio:${Versions.Ktor}"
    }

    object Coroutines {
        const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutines}"
        const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Coroutines}"
    }

    object Testing {
        const val Ktor = "io.ktor:ktor-client-mock:${Versions.Ktor}"
        const val Compose = "org.jetbrains.compose.ui:ui-test-junit4:${Versions.Compose}"
        const val MokoResources = "dev.icerock.moko:resources:${Versions.MokoResources}"
    }

    object MokoResources {
        const val Core = "dev.icerock.moko:resources:${Versions.MokoResources}"
        const val Test = "dev.icerock.moko:resources-test:${Versions.MokoResources}"
    }

    object XmlUtil {
        const val Serialization = "io.github.pdvrieze.xmlutil:serialization:${Versions.XmlUtil}"
    }

}

object Versions {

    const val Kotlin = "1.8.20"
    const val Ktor = "2.3.0"
    const val Coroutines = "1.6.4"
    const val Compose = "1.4.0"
    const val AGP = "7.4.2"
    const val MokoResources = "0.20.1"
    const val XmlUtil = "0.86.0"


    object Android {
        const val ActivityCompose = "1.7.1"
        const val Appcompat = "1.6.1"
        const val Material = "1.8.0"
        const val Annotation = "1.6.0"
    }

}