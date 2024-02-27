package controller

import io.ktor.server.application.*

interface AuthController {

    suspend fun ApplicationCall.register()
    suspend fun ApplicationCall.login()
    suspend fun ApplicationCall.logout()
}