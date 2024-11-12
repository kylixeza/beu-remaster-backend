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
        val preferConsumeAt = getPreferConsumeAt()

        val preferredRecipes  = repository.getPreferredRecipesByConsumeTime(uid, preferConsumeAt)
        val healthyRecipes = repository.getHealthyRecipes(uid)
        val bestRecipes = repository.getBestRecipes(uid)

        val preferredBaseHomeResponse = HomeRecipeResponse(
            when(preferConsumeAt) {
                PreferConsumeAt.BREAKFAST -> "What should I have for breakfast? 🌞🍳"
                PreferConsumeAt.LUNCH -> "It's time for lunch! 🍱"
                PreferConsumeAt.SNACK -> "Feel hungry? Let's make some snacks! 🍪"
                PreferConsumeAt.DINNER -> "Dinner time! 🌝🍜"
            },
            subtitle = "Check out these recommendations!",
            recipes = preferredRecipes
        )

        val healthyBaseHomeResponse = HomeRecipeResponse(
            title = "Healthy Lifestyle Choices 🥗",
            subtitle = null,
            recipes = healthyRecipes
        )

        val bestBaseHomeResponse = HomeRecipeResponse(
            title = "Best Recipes 🤩",
            subtitle = "Cooking recipes with the best reviews!",
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