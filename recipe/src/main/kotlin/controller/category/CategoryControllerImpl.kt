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
        val body = try {
            receive<List<CategoryRequest>>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        body.forEach {
            if (it.name.isBlank()) {
                buildErrorResponse(HttpStatusCode.BadRequest, "Nama kategori tidak boleh kosong")
                return
            }
            buildSuccessResponse("Kategori berhasil ditambahkan") { repository.insertCategory(it)  }
        }
    }

    override suspend fun ApplicationCall.insertCategoryRecipe() {
        val body = try {
            receive<CategoryRecipeRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        buildSuccessResponse("Kategori untuk resep berhasil ditambahkan") { repository.insertCategoryRecipe(body) }
    }

    override suspend fun ApplicationCall.getCategories() {
        buildSuccessResponse { repository.getAllCategories()}
    }
}