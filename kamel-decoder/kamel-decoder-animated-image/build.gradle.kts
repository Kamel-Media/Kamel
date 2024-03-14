import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.com.vanniktech.maven.publish)
}


kotlin {
    explicitApi = ExplicitApiMode.Warning

    jvm()
    js(IR) {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosX64()
    macosArm64()
    applyDefaultHierarchyTemplate()
    sourceSets {

        commonMain {
            dependencies {
                implementation(projects.kamelCore)
//              todo: remove ktor dependency related to https://github.com/Kamel-Media/Kamel/issues/35
                implementation(libs.ktor.client.core)
                implementation(compose.runtime)
                implementation(compose.foundation)
            }
        }
    }
}
