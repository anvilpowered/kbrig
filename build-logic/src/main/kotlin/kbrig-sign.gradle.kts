import org.jetbrains.kotlin.gradle.tooling.BuildKotlinToolingMetadataTask

plugins {
    signing
}

extensions.configure<PublishingExtension> {
    extensions.configure<SigningExtension> {
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
