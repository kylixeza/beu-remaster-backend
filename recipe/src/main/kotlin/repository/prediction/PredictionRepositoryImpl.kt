package repository.prediction

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import database.getBaseGroupBy
import database.getBaseQuery
import kotlinx.datetime.TimeZone
import model.prediction.PredictionResultRequest
import model.recipe.RecipeListResponse
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
    private val cloudStorageService: CloudStorageService
): PredictionRepository {
    override suspend fun insertPredictionResult(request: PredictionResultRequest, fileByte: ByteArray) {
        db.dbQuery {
            val url = cloudStorageService.run { fileByte.uploadFile("prediction/${request.predicted}") }

            PredictionResultTable.insert {
                it[predictionId] = "PREDICTION-${NanoIdUtils.randomNanoId()}"
                it[timestamp] = createTimeStamp(TimeZone.of("Asia/Jakarta"))
                it[expected] = request.expected
                it[predicted] = request.predicted
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
}