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
                buildErrorResponse(HttpStatusCode.BadRequest,"Nutrition name cannot be empty")
                return
            }

            buildSuccessResponse("Nutrition added successfully") { repository.insertNutrition(it) }
        }
    }

    override suspend fun ApplicationCall.insertNutritionRecipe() {
        val body = try {
            receive<NutritionRecipeRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        buildSuccessResponse("Nutrition for this recipe was added successfully") { repository.insertNutritionRecipe(body) }
    }
}