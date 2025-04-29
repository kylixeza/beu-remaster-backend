package repository.prediction

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import database.getBaseGroupBy
import database.getBaseQuery
import kotlinx.datetime.TimeZone
import model.prediction.PredictionResultRequest
import model.recipe.RecipeListResponse
import open_ai.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import storage.CloudStorageService
import tables.CategoryRecipeTable
import tables.PredictionResultTable
import tables.RecipeTable
import util.createTimeStamp
import util.toRecipeListResponse
import java.util.*

class PredictionRepositoryImpl(
    private val db: DatabaseFactory,
    private val cloudStorageService: CloudStorageService,
    private val openApiService: OpenApiService
): PredictionRepository {
    override suspend fun insertPredictionResult(request: PredictionResultRequest, fileByte: ByteArray) {
        db.dbQuery {
            val url = cloudStorageService.run { fileByte.uploadFile("prediction/${request.prediction}") }

            PredictionResultTable.insert {
                it[predictionId] = "PREDICTION-${NanoIdUtils.randomNanoId()}"
                it[timestamp] = createTimeStamp(TimeZone.of("Asia/Jakarta"))
                it[prediction] = request.prediction
                it[actual] = request.actual
                it[probability] = request.probability
                it[image] = url
            }
        }
    }

    override suspend fun getRelatedRecipes(uid: String, query: String): List<RecipeListResponse> {
        return db.dbQuery {
            val recipesByName = getBaseQuery().select {
                RecipeTable.name.lowerCase().like("%$query%".lowercase(Locale.getDefault()))
            }.getBaseGroupBy().map { it.toRecipeListResponse(uid) }

            val categoryIds = CategoryRecipeTable.select {
                CategoryRecipeTable.recipeId inList recipesByName.map { it.recipeId }
            }.map { it[CategoryRecipeTable.categoryId] }.distinctBy { it }

            val recipeIdsByCategory = CategoryRecipeTable.select {
                CategoryRecipeTable.categoryId inList categoryIds
            }.map { it[CategoryRecipeTable.recipeId] }

            val recipesByCategory = getBaseQuery().select {
                RecipeTable.recipeId inList recipeIdsByCategory
            }.getBaseGroupBy().map { it.toRecipeListResponse(uid) }

            (recipesByName + recipesByCategory).distinctBy { it.recipeId }
        }
    }

    override suspend fun classifyImage(uid: String, fileBytes: ByteArray): String {
        val url = cloudStorageService.run { fileBytes.uploadFile("classification/$uid/") }
        val request = ChatRequest(
            model = "gpt-4.1-mini",
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = listOf(
                        ChatMessageContent(
                            type = "text",
                            text = "Describe what this picture is. Answer directly, no explanation, no period, and use the case format Aaaa Bbbb. If it is not classified as an image of food, return the words Not A Food"
                        ),
                        ChatMessageContent(
                            type = "image_url",
                            imageUrl = ChatMessageContentImageUrl(
                                url,
                                "high"
                            )
                        )
                    )
                )
            ),
            maxTokens = 1000,
        )
        return openApiService.getChatResponse(request)
    }
}