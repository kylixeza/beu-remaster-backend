package controller.recipe

import io.ktor.server.application.*

interface RecipeController {
    suspend fun ApplicationCall.insertRecipe()
    suspend fun ApplicationCall.getHomeRecipes()
    suspend fun ApplicationCall.getRecipesByCategory(categoryId: String)
    suspend fun ApplicationCall.getDetailRecipe(uid: String, recipeId: String)
}