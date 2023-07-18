plugins {
    id("kotlin-jvm.base-conventions")
    id("kotlin-js.base-conventions")
    id("kbrig-publish")
    id("kbrig-sign")
}

dependencies {
    commonMainImplementation(libs.kotlinx.coroutines)
}
