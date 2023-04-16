plugins {
    id("kotlin-jvm.base-conventions")
    id("kbrig-publish")
}

dependencies {
    commonMainApi(project(":kbrig-core"))
    commonMainImplementation(libs.kotlinx.coroutines)
    // consumers of this library should be able to depend on different versions of brigadier
    jvmMainImplementation(libs.brigadier)
}
