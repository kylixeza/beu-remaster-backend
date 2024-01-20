package route

import Middleware
import base.BaseRoute
import controller.ReviewController
import io.ktor.server.routing.*

class ReviewRoute(
    private val controller: ReviewController,
    private val middleware: Middleware
): BaseRoute {
    override fun Route.routes() {
        route("/review") {

            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    val historyId = call.parameters["historyId"].orEmpty()
                    controller.apply { call.insertReview(uid, historyId) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    val historyId = call.parameters["historyId"].orEmpty()
                    controller.apply { call.getReviewByHistoryId(historyId) }
                }
            }

        }
    }
}