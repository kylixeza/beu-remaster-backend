package com.kylix

import configureHTTP
import configureInjection
import configureRouting
import configureSecurity
import configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT").toInt(),
        host = if (System.getenv("ENV") == "DEV") "localhost" else "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureInjection()
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureHTTP()
}
