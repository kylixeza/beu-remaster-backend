plugins {
    kotlin("jvm") version Version.kotlinVersion
    id("io.ktor.plugin") version Version.ktorVersion
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
    implementation(Libs.Ktor.gson)
    implementation(Libs.Ktor.auth)
    implementation(Libs.Ktor.jwt)
    implementation(Libs.Ktor.cn)
    implementation(Libs.Koin.koin)
    implementation(Libs.Koin.koinKtor)
    implementation(Libs.Exposed.core)

    implementation(project(Modules.core))
    implementation(project(Modules.auth))
    implementation(project(Modules.recipe))
    implementation(project(Modules.middleware))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}