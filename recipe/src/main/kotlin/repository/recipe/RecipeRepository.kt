package repository.recipe

import model.recipe.RecipeDetailResponse
import model.recipe.RecipeListResponse
import model.recipe.RecipeRequest

interface RecipeRepository {
    suspend fun insertRecipe(request: RecipeRequest)
    suspend fun getPreferredRecipesByConsumeTime(uid: String): List<RecipeListResponse>
    suspend fun getHealthyRecipes(uid: String): List<RecipeListResponse>
    suspend fun getBestRecipes(uid: String): List<RecipeListResponse>
    suspend fun getRecipesByCategory(uid: String, categoryId: String): List<RecipeListResponse>
    suspend fun getDetailRecipe(uid: String, recipeId: String): RecipeDetailResponse
}