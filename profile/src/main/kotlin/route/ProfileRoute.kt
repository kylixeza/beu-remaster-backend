package route

import HTTPVerb
import Middleware
import controller.ProfileController
import io.ktor.server.routing.*

class ProfileRoute(
    private val controller: ProfileController,
    private val middleware: Middleware
) {

    fun Route.profile() {
        route("/profile") {

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    controller.apply { call.getUser(uid) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.PUT) { uid, call ->
                    controller.apply { call.updateUser(uid) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    controller.apply { call.resetPassword(uid) }
                }
            }
        }
    }

}