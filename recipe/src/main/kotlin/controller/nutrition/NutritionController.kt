package controller.nutrition

import io.ktor.server.application.*

interface NutritionController {
    suspend fun ApplicationCall.insertNutrition()
    suspend fun ApplicationCall.insertNutritionRecipe()
}