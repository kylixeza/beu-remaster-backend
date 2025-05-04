package repository.recognition

import model.prediction.PredictionResultRequest
import model.recipe.RecipeListResponse

interface RecognitionRepository {
    suspend fun insertRecognitionResult(request: PredictionResultRequest, fileByte: ByteArray)
    suspend fun getRelatedRecipes(uid: String, food: String, category: String?): List<RecipeListResponse>
    suspend fun recognizeImage(uid: String, fileBytes: ByteArray): String
}