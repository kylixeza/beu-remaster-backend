plugins {
    kotlin("jvm") version Version.kotlinVersion
    id("io.ktor.plugin") version Version.ktorVersion
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

    implementation(Libs.Slf4j.api)
    implementation(Libs.Slf4j.simple)

    implementation(project(Modules.plugins))
}
