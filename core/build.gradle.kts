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
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}