import java.net.URI

plugins {
    `maven-publish`
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            credentials {
                username = project.findProperty("sonatypeUsername") as? String
                password = project.findProperty("sonatypePassword") as? String
            }
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots"
            url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
    publications.withType<MavenPublication> {
        pom {
            name.set("kbrig")
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
