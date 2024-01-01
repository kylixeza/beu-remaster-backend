plugins {
    kotlin("jvm")
}

group = App.group
version = App.version

repositories {
    mavenCentral()
}

dependencies {

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}