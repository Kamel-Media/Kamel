import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.vanniktech.maven.publish)
}

kotlin {
    explicitApi = ExplicitApiMode.Warning
    jvm()
    applyDefaultHierarchyTemplate()
    sourceSets {
        val jvmMain by getting {
            dependsOn(commonMain.get())
            dependencies {
                implementation(projects.kamelCore)
                // todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
                implementation(compose.ui)
                implementation(libs.apache.batik.transcoder)
                // https://stackoverflow.com/a/45318410/1363742
                implementation(libs.apache.batik.codec)
            }
        }
    }
}
