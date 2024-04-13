package repository.prediction

import model.prediction.PredictionResultRequest
import model.recipe.RecipeListResponse

interface PredictionRepository {
    suspend fun insertPredictionResult(request: PredictionResultRequest, fileByte: ByteArray)
    suspend fun getRelatedRecipes(uid: String, query: String): List<RecipeListResponse>
}