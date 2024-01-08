package controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import model.auth.LoginRequest
import model.auth.RegisterRequest
import model.auth.TokenResponse

interface AuthController {

    suspend fun ApplicationCall.register(body: RegisterRequest)
    suspend fun ApplicationCall.login(body: LoginRequest)
    suspend fun ApplicationCall.logout(token: String)
}