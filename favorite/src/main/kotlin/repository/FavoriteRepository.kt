package repository

import model.recipe.RecipeListResponse

interface FavoriteRepository {
    suspend fun insertFavorite(uid: String, recipeId: String)
    suspend fun deleteFavorite(uid: String, recipeId: String)
    suspend fun getFavorites(uid: String): List<RecipeListResponse>
}