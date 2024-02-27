package controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import model.auth.LoginRequest
import model.auth.RegisterRequest
import model.auth.TokenResponse

interface AuthController {

    suspend fun ApplicationCall.register()
    suspend fun ApplicationCall.login()
    suspend fun ApplicationCall.logout()
}