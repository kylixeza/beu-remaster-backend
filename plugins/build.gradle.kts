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
    implementation(Libs.Ktor.doubleReceive)
    implementation(Libs.Koin.koin)
    implementation(Libs.Koin.koinKtor)
    implementation(Libs.Exposed.core)

    implementation(project(Modules.core))
    implementation(project(Modules.auth))
    implementation(project(Modules.recipe))
    implementation(project(Modules.comment))
    implementation(project(Modules.history))
    implementation(project(Modules.review))
    implementation(project(Modules.middleware))
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-double-receive-jvm:2.3.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}