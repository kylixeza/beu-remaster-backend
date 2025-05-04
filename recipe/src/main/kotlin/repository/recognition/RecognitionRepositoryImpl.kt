package repository.recognition

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
import org.jetbrains.exposed.sql.selectAll
import storage.CloudStorageService
import tables.CategoryRecipeTable
import tables.CategoryTable
import tables.PredictionResultTable
import tables.RecipeTable
import util.createTimeStamp
import util.toRecipeListResponse
import java.util.*

class RecognitionRepositoryImpl(
    private val db: DatabaseFactory,
    private val cloudStorageService: CloudStorageService,
    private val openApiService: OpenApiService
): RecognitionRepository {
    override suspend fun insertRecognitionResult(request: PredictionResultRequest, fileByte: ByteArray) {
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

    override suspend fun getRelatedRecipes(
        uid: String,
        food: String,
        category: String?,
    ): List<RecipeListResponse> {
        return db.dbQuery {
            val recipesByName = getBaseQuery().select {
                RecipeTable.name.lowerCase().like("%$food%".lowercase(Locale.getDefault()))
            }.getBaseGroupBy().map { it.toRecipeListResponse(uid) }

            val recipeIdsByCategory = if (category == null) {
                val categoryIds = CategoryRecipeTable.select {
                    CategoryRecipeTable.recipeId inList recipesByName.map { it.recipeId }
                }.map { it[CategoryRecipeTable.categoryId] }.distinctBy { it }

                CategoryRecipeTable.select {
                    CategoryRecipeTable.categoryId inList categoryIds
                }.map { it[CategoryRecipeTable.recipeId] }
            } else {
                val categoryId = CategoryTable.select {
                    CategoryTable.name.lowerCase() eq category.lowercase()
                }.map { it[CategoryTable.categoryId] }.firstOrNull().orEmpty()

                CategoryRecipeTable.select {
                    CategoryRecipeTable.categoryId eq categoryId
                }.map { it[CategoryRecipeTable.recipeId] }
            }

            val recipesByCategory = getBaseQuery().select {
                RecipeTable.recipeId inList recipeIdsByCategory
            }.getBaseGroupBy().map { it.toRecipeListResponse(uid) }

            (recipesByName + recipesByCategory).distinctBy { it.recipeId }
        }
    }

    override suspend fun recognizeImage(
        uid: String,
        fileBytes: ByteArray,
    ): String {
        val availableCategories = db.dbQuery {
            CategoryTable.selectAll().map { it[CategoryTable.name] }
        }

        val base64Image = Base64.getEncoder().encodeToString(fileBytes)
        val request = ChatRequest(
            model = "gpt-4.1-mini",
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = listOf(
                        ChatMessageContent(
                            type = "text",
                            text =
                            """
                                Describe what this picture is. Answer directly, no explanation, no period, and use the case format Aaaa Bbbb. 
                                The result must be translated into Indonesian. 
                                If it is not classified as an image of food, return the words Not A Food. But do not translate Not A Food into Indonesian. 
                                Next, from these existing categories: ${availableCategories.joinToString(", ")} which one has the closest meaning to the image? 
                                Return the result into this json format
                                {
                                    "result": "Aaaa Bbbb",
                                    "closest_category": "category"
                                }
                            """.trimIndent()

                        ),
                        ChatMessageContent(
                            type = "image_url",
                            imageUrl = ChatMessageContentImageUrl(
                                "data:image/jpeg;base64,$base64Image",
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