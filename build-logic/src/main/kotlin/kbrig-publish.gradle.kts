import java.net.URI

plugins {
    `maven-publish`
    id("org.jetbrains.dokka")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
    publications.withType<MavenPublication> {
        artifact(javadocJar)
        pom {
            name.set("kbrig")
            description.set("A modern Kotlin rewrite of Brigadier's command building API with a focus on multiplatform support")
            url.set("https://www.anvilpowered.org")
            scm {
                url.set("https://github.com/anvilpowered/kbrig")
                connection.set("scm:git:https://github.com/anvilpowered/kbrig.git")
                developerConnection.set("scm:git:https://github.com/anvilpowered/kbrig.git")
            }
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                    distribution.set("repo")
                }
            }
            developers {
                rootProject.file("authors").readLines()
                    .asSequence()
                    .map { it.split(",") }
                    .forEach { (_id, _name) -> developer { id.set(_id); name.set(_name) } }
            }
        }
    }
}
