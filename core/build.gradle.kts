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
    implementation(Libs.Ktor.gson)
    implementation(Libs.Ktor.jwt)
    implementation(Libs.Database.hikari)
    implementation(Libs.Database.postgresql)
    implementation(Libs.Exposed.core)
    implementation(Libs.Exposed.jdbc)
    implementation(Libs.Exposed.dateTime)
    implementation(Libs.Koin.koin)
    implementation(Libs.Util.commonsCodec)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}