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
                authenticate(HTTPVerb.GET, "/home") { _, call ->
                    recipeController.apply { call.getHomeRecipes() }
                }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET, "/categories/{categoryId}") { _, call ->
                    recipeController.apply { call.getRecipesByCategory(call.parameters["categoryId"]!!) }
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