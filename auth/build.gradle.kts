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
    implementation(Libs.Ktor.gson)
    implementation(Libs.Koin.koin)
    implementation(Libs.Exposed.core)

    api(project(Modules.middleware))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}