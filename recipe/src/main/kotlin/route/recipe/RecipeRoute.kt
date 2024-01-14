package route.recipe

import Middleware
import controller.recipe.RecipeController
import io.ktor.server.application.*
import io.ktor.server.routing.*

class RecipeRoute(
    private val recipeController: RecipeController,
    private val middleware: Middleware
) {

    fun Route.recipes() {

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

            middleware.apply {
                authenticate(HTTPVerb.GET, "/{recipeId}") { uid, call ->
                    val recipeId = call.parameters["recipeId"].orEmpty()
                    recipeController.apply { call.getDetailRecipe(uid, recipeId) }
                }
            }
        }

    }

}