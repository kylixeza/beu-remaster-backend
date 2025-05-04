package route.prediction

import Middleware
import controller.recognition.RecognitionController
import io.ktor.server.routing.*

class RecognitionRoute(
    private val recognitionController: RecognitionController,
    private val middleware: Middleware,
) {

    fun Route.prediction() {
        route("/prediction") {

            middleware.apply {
                authenticate(HTTPVerb.POST) { _, call ->
                    recognitionController.apply { call.insertRecognitionResult() }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    val query = call.request.queryParameters["query"].orEmpty()
                    recognitionController.apply { call.getRelatedRecipes(uid, query) }
                }
            }
        }

        route("/recognition") {
            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    recognitionController.apply { call.recognizeImage(uid) }
                }
            }
        }
    }

}