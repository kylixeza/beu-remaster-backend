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

    override suspend fun ApplicationCall.searchRecipes(uid: String, query: String) {
        buildSuccessListResponse { repository.searchRecipes(uid, query) }
    }

    override suspend fun ApplicationCall.getHomeRecipes(uid: String) {
        val preferredRecipes  = repository.getPreferredRecipesByConsumeTime(uid)
        val healthyRecipes = repository.getHealthyRecipes(uid)
        val bestRecipes = repository.getBestRecipes(uid)

        val preferredBaseHomeResponse = HomeRecipeResponse(
            when(getPreferConsumeAt()) {
                PreferConsumeAt.BREAKFAST -> "Pagi-Pagi Enaknya Sarapan Apa Ya? 🌞🍳"
                PreferConsumeAt.LUNCH -> "Udah Waktunya Makan Siang Nih! 🍱"
                PreferConsumeAt.SNACK -> "Lagi Lapar? Nyemil Aja! 🍪"
                PreferConsumeAt.DINNER -> "Malam-Malam Laper Ya? 🌝🍜"
            },
            subtitle = "Cek rekomendasi berikut!",
            recipes = preferredRecipes
        )

        val healthyBaseHomeResponse = HomeRecipeResponse(
            title = "Pilihan Hidup Sehat 🥗",
            subtitle = null,
            recipes = healthyRecipes
        )

        val bestBaseHomeResponse = HomeRecipeResponse(
            title = "Resep Terbaik 🤩",
            subtitle = "Resep masakan dengan ulasan terbaik!",
            recipes = bestRecipes
        )

        val combined = listOf(preferredBaseHomeResponse, healthyBaseHomeResponse, bestBaseHomeResponse)

        buildSuccessListResponse { combined }

    }

    override suspend fun ApplicationCall.getRecipesByCategory(uid: String, categoryId: String) {
        buildSuccessListResponse { repository.getRecipesByCategory(uid, categoryId) }
    }

    override suspend fun ApplicationCall.getDetailRecipe(uid: String, recipeId: String) {
        buildSuccessResponse { repository.getDetailRecipe(uid, recipeId) }
    }
}