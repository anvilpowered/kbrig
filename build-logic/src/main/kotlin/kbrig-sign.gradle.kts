import org.jetbrains.kotlin.gradle.tooling.BuildKotlinToolingMetadataTask

plugins {
    signing
}

extensions.configure<PublishingExtension> {
    if (System.getenv("AGENT_NAME")?.contains("publishing") != true) {
        return@configure
    }
    extensions.configure<SigningExtension> {
        useInMemoryPgpKeys(
            /* defaultKeyId = */ System.getenv("SIGNING_KEY_ID"),
            /* defaultSecretKey = */ System.getenv("SIGNING_KEY"),
            /* defaultPassword = */ System.getenv("SIGNING_PASSWORD"),
        )
        tasks.withType<BuildKotlinToolingMetadataTask> {
            sign(outputFile)
        }
        publications.withType<MavenPublication> {
            val signingTasks = sign(this)
            tasks.withType<AbstractPublishToMaven> {
                dependsOn(signingTasks)
            }
        }
    }
}
