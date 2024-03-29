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
    implementation(Libs.Ktor.auth)
    implementation(Libs.Ktor.jwt)
    implementation(Libs.Koin.koin)

    api(project(Modules.common))
    api(project(Modules.core))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}