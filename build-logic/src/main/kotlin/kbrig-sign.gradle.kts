plugins {
    signing
}

extensions.configure<PublishingExtension> {
    publications.withType<MavenPublication> {
        val publication = this
        extensions.configure<SigningExtension> {
            val signingTasks = sign(publication)
            tasks.withType<AbstractPublishToMaven>().configureEach {
                dependsOn(signingTasks)
            }
        }
    }
}
