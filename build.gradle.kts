plugins {
    kotlin("jvm") version Version.kotlinVersion
    id("io.ktor.plugin") version Version.ktorVersion
    id("org.jetbrains.kotlin.plugin.serialization") version Version.kotlinVersion
}

group = App.group
version = App.version

application {
    mainClass.set("com.kylix.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Libs.Ktor.core)
    implementation(Libs.Ktor.netty)

    api(project(Modules.plugins))
}
