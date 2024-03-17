package route.recipe

import HTTPVerb
import Middleware
import base.BaseRoute
import controller.recipe.RecipeController
import io.ktor.server.application.*
import io.ktor.server.routing.*

class RecipeRoute(
    private val recipeController: RecipeController,
    private val middleware: Middleware
) {

    fun Route.recipes(
        vararg routesUnderRecipes: BaseRoute
    ) {

        route("/recipes") {

            post {
                recipeController.apply { call.insertRecipe() }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { uid, call ->
                    val query = call.request.queryParameters["query"].orEmpty()
                    recipeController.apply { call.searchRecipes(uid, query) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET, "/home") { uid, call ->
                    recipeController.apply { call.getHomeRecipes(uid) }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET, "/categories/{categoryId}") { uid, call ->
                    val categoryId = call.parameters["categoryId"].orEmpty()
                    recipeController.apply { call.getRecipesByCategory(uid, categoryId) }
                }
            }

            route("/{recipeId}") {
                middleware.apply {
                    authenticate(HTTPVerb.GET) { uid, call ->
                        val recipeId = call.parameters["recipeId"].orEmpty()
                        recipeController.apply { call.getDetailRecipe(uid, recipeId) }
                    }
                }

                routesUnderRecipes.forEach {
                    it.apply { routes() }
                }
            }
        }

    }

}