@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://libraries.minecraft.net")
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kbrig"

sequenceOf(
    "brigadier",
    "core",
).forEach {
    val project = ":${rootProject.name}-$it"
    include(project)
    project(project).projectDir = file(it.replace('-', '/'))
}
