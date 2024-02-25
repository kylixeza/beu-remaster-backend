package controller.recipe

import base.buildErrorResponse
import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.recipe.HomeRecipeResponse
import model.recipe.RecipeRequest
import repository.recipe.RecipeRepository
import util.getPreferConsumeAt

class RecipeControllerImpl(
    private val repository: RecipeRepository
): RecipeController {
    override suspend fun ApplicationCall.insertRecipe() {
        val body = try {
            receive<List<RecipeRequest>>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        body.forEach { recipe ->
            if (recipe.name.isBlank() || recipe.description.isBlank() || recipe.difficulty.isBlank() ||
                recipe.image.isBlank() || recipe.startEstimation < 0 || recipe.endEstimation < 0 ||
                recipe.estimationUnit.isBlank() || recipe.video.isBlank() || recipe.preferConsumeAt.isBlank() ||
                recipe.ingredients.isEmpty() || recipe.tools.isEmpty() || recipe.steps.isEmpty() ||
                recipe.nutrition.isEmpty() || recipe.categories.isNullOrEmpty()
            ) {
                buildErrorResponse(HttpStatusCode.BadRequest, "Invalid request body")
                return
            }
            buildSuccessResponse { repository.insertRecipe(recipe) }
        }
    }

    override suspend fun ApplicationCall.getHomeRecipes() {
        val preferredRecipes  = repository.getPreferredRecipesByConsumeTime()
        val healthyRecipes = repository.getHealthyRecipes()
        val bestRecipes = repository.getBestRecipes()

        val preferredTranslate = getPreferConsumeAt().translate

        val preferredBaseHomeResponse = HomeRecipeResponse(
            title = "Masak Untuk $preferredTranslate?",
            subtitle = "Cek rekomendasi berikut!",
            recipes = preferredRecipes
        )

        val healthyBaseHomeResponse = HomeRecipeResponse(
            title = "Pilihan Hidup Sehat",
            subtitle = null,
            recipes = healthyRecipes
        )

        val bestBaseHomeResponse = HomeRecipeResponse(
            title = "Makanan Terbaik Kami",
            subtitle = "Menu masakan dengan ulasan terbaik!",
            recipes = bestRecipes
        )

        val combined = listOf(preferredBaseHomeResponse, healthyBaseHomeResponse, bestBaseHomeResponse)

        buildSuccessListResponse { combined }

    }

    override suspend fun ApplicationCall.getRecipesByCategory(categoryId: String) {
        buildSuccessListResponse { repository.getRecipesByCategory(categoryId) }
    }

    override suspend fun ApplicationCall.getDetailRecipe(uid: String, recipeId: String) {
        buildSuccessResponse { repository.getDetailRecipe(uid, recipeId) }
    }
}