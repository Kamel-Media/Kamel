import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("org.jetbrains.dokka") version "1.4.20"
    id("io.codearte.nexus-staging") version ("0.22.0")
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        maven(url = "https://kotlin.bintray.com/ktor")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:0.3.0-build154")
        classpath("com.android.tools.build:gradle:7.0.0-alpha06")
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
    }
}

ext {
    set("GroupId", "com.alialbaali.kamel")
    set("Version", "0.1.2-SNAPSHOT")
}

val file = project.file("local.properties")

if (file.exists()) {
    Properties().apply {
        val inputStream = file.inputStream()
        load(inputStream)
        ext {
            set("signing.keyId", getProperty("signing.keyId"))
            set("signing.password", getProperty("signing.password"))
            set("ossrh.username", getProperty("ossrh.username"))
            set("ossrh.password", getProperty("ossrh.password"))
            set("signing.secretKeyRingFile", getProperty("signing.secretKeyRingFile"))
            set("stagingProfileId", getProperty("stagingProfileId"))
        }
        inputStream.close()
    }
} else {
    ext {
        set("signing.keyId", System.getenv("SIGNING_KEY_ID"))
        set("signing.password", System.getenv("SIGNING_PASSWORD"))
        set("ossrh.username", System.getenv("OSSRH_USERNAME"))
        set("ossrh.password", System.getenv("OSSRH_PASSWORD"))
        set("signing.secretKeyRingFile", System.getenv("SIGNING_SECRET_KEY_RING_FILE"))
        set("stagingProfileId", System.getenv("STAGING_PROFILE_ID"))
    }
}

allprojects {

    group = rootProject.ext["GroupId"] as String
    version = rootProject.ext["Version"] as String

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        maven(url = "https://kotlin.bintray.com/ktor")
    }

    val emptyJavadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    afterEvaluate {
        extensions.findByType<PublishingExtension>()?.apply {
            publications.withType<MavenPublication>().configureEach {

                artifact(emptyJavadocJar.get())

                pom {

                    name.set("Kamel")
                    description.set("Kotlin Asynchronous Media Loading Library that supports Compose")
                    url.set("https://github.com/alialbaali/Kamel")

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("alialbaali")
                            name.set("Ali Albaali")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/alialbaali/Kamel.git")
                        developerConnection.set("scm:git:ssh://github.com/alialbaali/Kamel.git")
                        url.set("https://github.com/alialbaali/Kamel/tree/main")
                    }

                }

            }

            repositories {
                maven {

                    name = "MavenCentral"

                    val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

                    url = if (version.toString().endsWith("SNAPSHOT")) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)

                    credentials {
                        username = rootProject.ext["ossrh.username"] as String
                        password = rootProject.ext["ossrh.password"] as String
                    }

                }
            }

            extensions.findByType<SigningExtension>()?.sign(publications)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
        }
    }

}

nexusStaging {
    packageGroup = rootProject.ext["GroupId"] as String
    stagingProfileId = rootProject.ext["stagingProfileId"] as String
    username = rootProject.ext["ossrh.username"] as String
    password = rootProject.ext["ossrh.password"] as String
}
