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
    implementation(Libs.Exposed.core)
    implementation(Libs.Util.jnanoid)
    implementation(Libs.Util.dateTime)
    implementation(Libs.GoogleCloud.storage)

    implementation(Libs.Koin.koin)

    api(project(Modules.middleware))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}