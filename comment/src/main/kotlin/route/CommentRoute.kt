package route

import HTTPVerb
import Middleware
import base.BaseRoute
import controller.CommentController
import io.ktor.server.routing.*

class CommentRoute(
    private val repository: CommentController,
    private val middleware: Middleware
): BaseRoute {

    override fun Route.routes() {
        route("/comments") {

            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    val recipeId = call.parameters["recipeId"].orEmpty()
                    repository.apply { call.insertComment(recipeId, uid) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    val recipeId = call.parameters["recipeId"].orEmpty()
                    repository.apply { call.getComments(uid, recipeId) }
                }
            }
        }
    }
}