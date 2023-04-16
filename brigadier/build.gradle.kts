plugins {
    id("kotlin-jvm.base-conventions")
    id("kbrig-publish")
}

dependencies {
    commonMainApi(project(":kbrig-core"))
    commonMainImplementation(libs.kotlinx.coroutines)
    // consumers of this library should be able to depend on different versions of brigadier
    // also, brigadier is usually already included at runtime on most platforms
    // so don't add it to the runtime classpath of consumers
    jvmMainCompileOnly(libs.brigadier)
}
