plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ktlint)
    id("kotlin-jvm.base-conventions")
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    group = "org.anvilpowered"
    version = projectVersion
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
