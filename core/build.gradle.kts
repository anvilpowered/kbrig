plugins {
    id("kotlin-jvm.base-conventions")
    id("kotlin-js.base-conventions")
    id("kbrig-publish")
}

dependencies {
    commonMainImplementation(libs.kotlinx.coroutines)
}
