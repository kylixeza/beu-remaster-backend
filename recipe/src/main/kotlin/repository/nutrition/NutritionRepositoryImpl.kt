package repository.nutrition

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.nutrition.NutritionRecipeRequest
import model.nutrition.NutritionRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.NutritionRecipeTable
import tables.NutritionTable
import tables.RecipeTable

class NutritionRepositoryImpl(
    private val db: DatabaseFactory
): NutritionRepository {
    override suspend fun insertNutrition(request: NutritionRequest) {
        db.dbQuery {
            val nutritionId = "NUTRITION-${NanoIdUtils.randomNanoId()}"
            NutritionTable.insert {
                it[this.nutritionId] = nutritionId
                it[name] = request.name
                it[unit] = request.unit
            }
        }
    }

    override suspend fun insertNutritionRecipe(request: NutritionRecipeRequest) {
        db.dbQuery {
            val nutritionId = NutritionTable.select {
                NutritionTable.name eq request.nutritionName
            }.first()[NutritionTable.nutritionId]

            request.recipes.forEach {recipe ->
                val recipeId = RecipeTable.select {
                    RecipeTable.name eq recipe.key
                }.first()[RecipeTable.recipeId]

                NutritionRecipeTable.insert {
                    it[this.nutritionId] = nutritionId
                    it[this.recipeId] = recipeId
                    it[this.amount] = recipe.value
                }
            }
        }
    }
}