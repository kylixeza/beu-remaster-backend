package controller.category

import io.ktor.server.application.*

interface CategoryController {
    suspend fun ApplicationCall.insertCategory()
    suspend fun ApplicationCall.insertCategoryRecipe()
    suspend fun ApplicationCall.getCategories()
}