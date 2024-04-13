package route.prediction

import Middleware
import controller.prediction.PredictionController
import io.ktor.server.routing.*

class PredictionRoute(
    private val predictionController: PredictionController,
    private val middleware: Middleware,
) {

    fun Route.prediction() {
        route("/prediction") {

            middleware.apply {
                authenticate(HTTPVerb.POST) { _, call ->
                    predictionController.apply { call.insertPredictionResult() }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    val query = call.request.queryParameters["query"].orEmpty()
                    predictionController.apply { call.getRelatedRecipes(uid, query) }
                }
            }
        }
    }

}