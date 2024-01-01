plugins {
    kotlin("jvm") version Version.kotlinVersion
    id("io.ktor.plugin") version Version.ktorVersion
    id("org.jetbrains.kotlin.plugin.serialization") version Version.kotlinVersion
}

group = App.group
version = App.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(Libs.Ktor.core)
    implementation(Libs.Ktor.cors)
    implementation(Libs.Ktor.resources)
    implementation(Libs.Ktor.serialization)
    implementation(Libs.Ktor.auth)
    implementation(Libs.Ktor.jwt)
    implementation(Libs.Ktor.cn)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}