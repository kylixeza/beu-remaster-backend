package route

import Middleware
import controller.HelpCenterController
import io.ktor.server.routing.*

class HelpCenterRoute(
    private val controller: HelpCenterController,
    private val middleware: Middleware
) {

    fun Route.helpCenter() {
        route("/help-center") {
            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    controller.apply { call.sendEmail(uid) }
                }
            }
        }
    }

}