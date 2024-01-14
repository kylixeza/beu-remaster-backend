package controller.nutrition

import base.buildErrorResponse
import base.buildSuccessResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.nutrition.NutritionRecipeRequest
import model.nutrition.NutritionRequest
import repository.nutrition.NutritionRepository

class NutritionControllerImpl(
    private val repository: NutritionRepository
): NutritionController {
    override suspend fun ApplicationCall.insertNutrition() {
        val body = try {
            receive<List<NutritionRequest>>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        body.forEach {
            if (it.name.isBlank()) {
                buildErrorResponse(HttpStatusCode.BadRequest,"Nama nutrisi tidak boleh kosong")
                return
            }

            buildSuccessResponse("Nutrisi berhasil ditambahkan") { repository.insertNutrition(it) }
        }
    }

    override suspend fun ApplicationCall.insertNutritionRecipe() {
        val body = try {
            receive<NutritionRecipeRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        buildSuccessResponse("Nutrisi untuk resep berhasil ditambahkan") { repository.insertNutritionRecipe(body) }
    }
}