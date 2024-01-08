package route

import controller.AuthController
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import model.auth.LoginRequest
import model.auth.RegisterRequest

class AuthRoute(
    private val controller: AuthController,
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

    fun Route.initRoutes() {
        register()
        login()
        logout()
    }
}