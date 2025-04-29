plugins {
    kotlin("jvm") version Version.kotlinVersion
    id("io.ktor.plugin") version Version.ktorVersion
    kotlin("plugin.serialization") version Version.kotlinVersion
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
    implementation(Libs.Ktor.cn)
    implementation(Libs.Ktor.serialization)
    implementation(Libs.KtorClient.core)
    implementation(Libs.KtorClient.cio)
    implementation(Libs.KtorClient.cn)
    implementation(Libs.Database.hikari)
    implementation(Libs.Database.postgresql)
    implementation(Libs.Exposed.core)
    implementation(Libs.Exposed.jdbc)
    implementation(Libs.Exposed.dateTime)
    implementation(Libs.Koin.koin)
    implementation(Libs.Util.commonsCodec)
    implementation(Libs.Util.jnanoid)
    implementation(Libs.GoogleCloud.storage)
    implementation(Libs.Jakarta.email)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}