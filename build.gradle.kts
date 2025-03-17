import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.com.vanniktech.maven.publish) apply false
}


allprojects {

    group = project.property("GROUP") as String
    version = project.property("VERSION_NAME") as String


    afterEvaluate {
        extensions.findByType<PublishingExtension>()?.apply {
            publications.withType<MavenPublication>().configureEach {

                pom {

                    name.set("Kamel")
                    description.set("Kotlin Asynchronous Media Loading Library that supports Compose")
                    url.set("https://github.com/Kamel-Media/Kamel")

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("alialbaali")
                            name.set("Ali Albaali")
                        }
                        developer {
                            id.set("luca992")
                            name.set("Luca Spinazzola")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/Kamel-Media/Kamel.git")
                        developerConnection.set("scm:git:ssh://github.com/Kamel-Media/Kamel.git")
                        url.set("https://github.com/Kamel-Media/Kamel/tree/main")
                    }

                }

            }

        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget = JvmTarget.JVM_11
    }
}