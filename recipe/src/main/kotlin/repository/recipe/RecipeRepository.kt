package repository.recipe

import model.recipe.RecipeDetailResponse
import model.recipe.RecipeListResponse
import model.recipe.RecipeRequest

interface RecipeRepository {
    suspend fun insertRecipe(request: RecipeRequest)
    suspend fun getPreferredRecipesByConsumeTime(): List<RecipeListResponse>
    suspend fun getHealthyRecipes(): List<RecipeListResponse>
    suspend fun getBestRecipes(): List<RecipeListResponse>
    suspend fun getRecipesByCategory(categoryId: String): List<RecipeListResponse>
    suspend fun getDetailRecipe(uid: String, recipeId: String): RecipeDetailResponse
}