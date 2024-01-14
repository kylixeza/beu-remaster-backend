package controller.recipe

import base.buildErrorResponse
import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.recipe.BaseHomeRecipeResponse
import model.recipe.HomeRecipeResponse
import model.recipe.RecipeRequest
import repository.recipe.RecipeRepository
import util.PreferConsumeAt
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

        val preferredBaseHomeResponse = BaseHomeRecipeResponse(
            title = "Masak Untuk $preferredTranslate?",
            subtitle = "Cek rekomendasi berikut!",
            recipes = preferredRecipes
        )

        val healthyBaseHomeResponse = BaseHomeRecipeResponse(
            title = "Pilihan Hidup Sehat",
            subtitle = null,
            recipes = healthyRecipes
        )

        val bestBaseHomeResponse = BaseHomeRecipeResponse(
            title = "Makanan Terbaik Kami",
            subtitle = "Menu masakan dengan ulasan terbaik!",
            recipes = bestRecipes
        )

        val homeRecipeResponse = HomeRecipeResponse(
            preferredRecipe = preferredBaseHomeResponse,
            healthyRecipe = healthyBaseHomeResponse,
            bestRecipe = bestBaseHomeResponse
        )

        buildSuccessResponse { homeRecipeResponse }
    }

    override suspend fun ApplicationCall.getRecipesByCategory(categoryId: String) {
        buildSuccessListResponse { repository.getRecipesByCategory(categoryId) }
    }

    override suspend fun ApplicationCall.getDetailRecipe(uid: String, recipeId: String) {
        buildSuccessResponse { repository.getDetailRecipe(uid, recipeId) }
    }
}