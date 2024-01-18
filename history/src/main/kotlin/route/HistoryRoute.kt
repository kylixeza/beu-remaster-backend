package route

import Middleware
import base.BaseRoute
import controller.HistoryController
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class HistoryRoute(
    private val controller: HistoryController,
    private val middleware: Middleware
): BaseRoute {

    override fun Route.routes() {
        route("/histories") {
            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    controller.apply { call.insertHistory(uid) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    controller.apply { call.getHistories(uid) }
                }
            }
        }
    }

}