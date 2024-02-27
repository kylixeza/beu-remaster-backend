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
            controller.apply { call.register() }
        }
    }

    private fun Route.login() {
        post("/login") {
            controller.apply { call.login() }
        }
    }

    private fun Route.logout() {
        post("/logout") {
            controller.apply { call.logout() }
        }
    }

    fun Route.auth() {
        register()
        login()
        logout()
    }
}