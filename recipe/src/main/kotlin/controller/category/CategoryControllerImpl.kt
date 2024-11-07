package controller.category

import base.buildErrorResponse
import base.buildSuccessResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.category.CategoryRecipeRequest
import model.category.CategoryRequest
import repository.category.CategoryRepository

class CategoryControllerImpl(
    private val repository: CategoryRepository,
): CategoryController {
    override suspend fun ApplicationCall.insertCategory() {
        val body = receive<List<CategoryRequest>>()
        body.forEach {
            if (it.name.isBlank()) {
                buildErrorResponse(HttpStatusCode.BadRequest, "Category name cannot be empty")
                return
            }
            buildSuccessResponse("Category was added successfully") { repository.insertCategory(it)  }
        }
    }

    override suspend fun ApplicationCall.insertCategoryRecipe() {
        val body = receive<CategoryRecipeRequest>()
        buildSuccessResponse("Category for this recipe was added successfully") { repository.insertCategoryRecipe(body) }
    }

    override suspend fun ApplicationCall.getCategories() {
        buildSuccessResponse { repository.getAllCategories()}
    }
}