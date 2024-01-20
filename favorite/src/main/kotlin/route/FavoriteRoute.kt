package route

import HTTPVerb
import Middleware
import base.BaseRoute
import controller.FavoriteController
import io.ktor.server.routing.*

class FavoriteRoute(
    private val controller: FavoriteController,
    private val middleware: Middleware,
): BaseRoute {

    fun Route.favorites() {
        route("favorites") {
            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    controller.apply { call.getFavorites(uid) }
                }
            }
        }
    }

    override fun Route.routes() {
        route("favorites") {

            middleware.apply {
                authenticate(HTTPVerb.POST) { uid, call ->
                    val recipeId = call.parameters["recipeId"].orEmpty()
                    controller.apply { call.insertFavorite(uid, recipeId) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.DELETE) { uid, call ->
                    val recipeId = call.parameters["recipeId"].orEmpty()
                    controller.apply { call.deleteFavorite(uid, recipeId) }
                }
            }
        }
    }

}