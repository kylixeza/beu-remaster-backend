plugins {
    kotlin("jvm") version Version.kotlinVersion
}

group = App.group
version = App.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(Libs.Database.hikari)
    implementation(Libs.Exposed.core)
    implementation(Libs.Exposed.jdbc)
    implementation(Libs.Exposed.dateTime)
    implementation(Libs.Koin.koin)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}