package route

import Middleware
import controller.AuthController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.auth.LoginRequest
import model.auth.RegisterRequest
import security.token.TokenService

class AuthRoute(
    private val controller: AuthController,
    private val middleware: Middleware,
) {

    private fun Route.register() {
        post("/register") {
            val body = call.receive<RegisterRequest>()
            controller.apply { call.register(body) }
        }
    }

    private fun Route.login() {
        post("/login") {
            val body = call.receive<LoginRequest>()
            controller.apply { call.login(body) }
        }
    }

    private fun Route.logout() {
        post("/logout") {
            val token = call.request.header("Authorization")?.substring("Bearer ".length).orEmpty()
            controller.apply { call.logout(token) }
        }
    }

    private fun Route.testJwt() = middleware.apply {
        authenticate(HTTPVerb.GET, "/test-jwt") { uid, call ->
            call.respondText("uid: $uid")
        }
    }

    fun Route.initRoutes() {
        register()
        login()
        logout()
        testJwt()
    }
}