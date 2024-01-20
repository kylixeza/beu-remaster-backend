package route

import Middleware
import base.BaseRoute
import controller.HistoryController
import io.ktor.server.routing.*

class HistoryRoute(
    private val controller: HistoryController,
    private val middleware: Middleware
) {

    fun Route.histories(
        vararg routesUnderHistories: BaseRoute
    ) {
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

            route("/{historyId}") {
                routesUnderHistories.forEach {
                    it.apply { routes() }
                }
            }
        }
    }

}