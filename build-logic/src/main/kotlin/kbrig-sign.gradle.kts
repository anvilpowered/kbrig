import org.jetbrains.kotlin.gradle.tooling.BuildKotlinToolingMetadataTask

plugins {
    signing
}

extensions.configure<PublishingExtension> {
    if (project.hasProperty("nosign")) {
        return@configure
    }
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
