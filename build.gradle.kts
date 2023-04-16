

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ktlint)
    id("kotlin-jvm.base-conventions")
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "kbrig-publish")
    group = "org.anvilpowered"
    version = projectVersion
    project.findProperty("buildNumber")
        ?.takeIf { version.toString().contains("SNAPSHOT") }
        ?.also { version = version.toString().replace("SNAPSHOT", "RC$it") }
    kotlin {
        targets.all {
            compilations.all {
                kotlinOptions {
                    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
                }
            }
        }
    }
}

dependencies {
    commonMainImplementation(kotlin("reflect"))
}
