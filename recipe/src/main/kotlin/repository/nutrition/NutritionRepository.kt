package repository.nutrition

import model.nutrition.NutritionRecipeRequest
import model.nutrition.NutritionRequest

interface NutritionRepository {
    suspend fun insertNutrition(request: NutritionRequest)
    suspend fun insertNutritionRecipe(request: NutritionRecipeRequest)
}