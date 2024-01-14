package repository.category

import model.category.CategoryRecipeRequest
import model.category.CategoryRequest
import model.category.CategoryResponse

interface CategoryRepository {
    suspend fun insertCategory(request: CategoryRequest)
    suspend fun insertCategoryRecipe(request: CategoryRecipeRequest)
   suspend fun getAllCategories(): List<CategoryResponse>
}