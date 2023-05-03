plugins {
    id("kotlin-jvm.base-conventions")
    id("kbrig-publish")
}

dependencies {
    commonMainImplementation(libs.kotlinx.coroutines)
}
