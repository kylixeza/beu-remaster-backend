package controller.recipe

import io.ktor.server.application.*

interface RecipeController {
    suspend fun ApplicationCall.insertRecipe()
    suspend fun ApplicationCall.searchRecipes(uid: String, query: String)
    suspend fun ApplicationCall.getHomeRecipes(uid: String)
    suspend fun ApplicationCall.getRecipesByCategory(uid: String, categoryId: String)
    suspend fun ApplicationCall.getDetailRecipe(uid: String, recipeId: String)
}