package repository.recipe

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import database.getBaseGroupBy
import database.getBaseQuery
import database.isFavorite
import model.recipe.RecipeDetailResponse
import model.recipe.RecipeListResponse
import model.recipe.RecipeRequest
import org.jetbrains.exposed.sql.*
import tables.*
import util.*

class RecipeRepositoryImpl(
    private val db: DatabaseFactory
): RecipeRepository {

    override suspend fun insertRecipe(request: RecipeRequest) {
        db.dbQuery {
            val recipeId = "RECIPE-${NanoIdUtils.randomNanoId()}"
            RecipeTable.insert {
                it[this.recipeId] = recipeId
                it[name] = request.name
                it[description] = request.description
                it[difficulty] = request.difficulty.enumerateRecipeDifficulty()
                it[image] = request.image
                it[startEstimation] = request.startEstimation
                it[endEstimation] = request.endEstimation
                it[estimationUnit] = request.estimationUnit
                it[video] = request.video
                it[preferConsumeAt] = request.preferConsumeAt.enumeratePreferConsumeAt()
            }
            request.ingredients.forEach { ingredient ->
                IngredientTable.insert {
                    it[this.recipeId] = recipeId
                    it[this.ingredient] = ingredient
                }
            }
            request.tools.forEach { tool ->
                ToolTable.insert {
                    it[this.recipeId] = recipeId
                    it[this.tool] = tool
                }
            }
            request.steps.forEach { step ->
                StepTable.insert {
                    it[this.recipeId] = recipeId
                    it[this.step] = step.value
                    it[this.order] = step.key
                }
            }

            request.nutrition.forEach { nutrition ->

                val nutritionId = NutritionTable.select { NutritionTable.name eq nutrition.key.replaceFirstChar { it.uppercaseChar() } }
                    .map { it[NutritionTable.nutritionId] }.firstOrNull()

                if (nutritionId != null) {
                    NutritionRecipeTable.insert {
                        it[this.nutritionId] = nutritionId
                        it[this.recipeId] = recipeId
                        it[this.amount] = nutrition.value
                    }
                }
            }

            request.categories?.forEach { category ->
                val categoryId = CategoryTable.select { CategoryTable.name eq category }
                    .map { it[CategoryTable.categoryId] }.firstOrNull()

                if (categoryId != null) {
                    CategoryRecipeTable.insert {
                        it[this.categoryId] = categoryId
                        it[this.recipeId] = recipeId
                    }
                }
            }
        }
    }

    override suspend fun getPreferredRecipesByConsumeTime(uid: String): List<RecipeListResponse> = db.dbQuery {
        val preferAt = getPreferConsumeAt()
        getBaseQuery().select {
            RecipeTable.preferConsumeAt.eq(preferAt)
        }.getBaseGroupBy().map { it.toRecipeListResponse(uid) }
    }

    override suspend fun getHealthyRecipes(uid: String): List<RecipeListResponse> {
        val categoryName = "Sayur"
        return db.dbQuery {
            val recipeIdByCategoryName = CategoryTable.join(CategoryRecipeTable, JoinType.INNER) {
                CategoryTable.categoryId eq CategoryRecipeTable.categoryId
            }.select { CategoryTable.name eq categoryName }.map { it[CategoryRecipeTable.categoryId] }
            getBaseQuery().selectAll()
                .getBaseGroupBy()
                .map { it.toRecipeListResponse(uid) }
                .filter { it.recipeId in recipeIdByCategoryName }
                .distinctBy { it.recipeId }
        }
    }

    override suspend fun getBestRecipes(uid: String): List<RecipeListResponse> {
        return db.dbQuery {
            getBaseQuery().selectAll()
                .getBaseGroupBy()
                .orderBy(Avg(ReviewTable.rating, 1), SortOrder.DESC)
                .map { it.toRecipeListResponse(uid) }
        }
    }

    override suspend fun getRecipesByCategory(uid: String, categoryId: String): List<RecipeListResponse> {
        return db.dbQuery {
            getBaseQuery()
                .select { CategoryRecipeTable.categoryId eq categoryId }
                .getBaseGroupBy()
                .map { it.toRecipeListResponse(uid) }
        }
    }

    override suspend fun getDetailRecipe(uid: String, recipeId: String): RecipeDetailResponse {
        return db.dbQuery {
            val ingredients = IngredientTable.select { IngredientTable.recipeId eq recipeId }.map { it[IngredientTable.ingredient] }
            val tools = ToolTable.select { ToolTable.recipeId eq recipeId }.map { it[ToolTable.tool] }
            val steps = StepTable.select { StepTable.recipeId eq recipeId }.map { it[StepTable.step] }

            val isFavorite = FavoriteTable.select { FavoriteTable.recipeId.eq(recipeId) and FavoriteTable.uid.eq(uid) }
                .map { it[FavoriteTable.recipeId] }.isNotEmpty()

            val nutrition = NutritionTable.join(NutritionRecipeTable, JoinType.INNER) {
                NutritionTable.nutritionId eq NutritionRecipeTable.nutritionId
            }.select { NutritionRecipeTable.recipeId eq recipeId }.map { it.toNutritionResponse() }


            val reviews = ReviewTable.join(UserTable, JoinType.INNER)
                .select { ReviewTable.recipeId.eq(recipeId) }
                .mapNotNull {
                    val reviewId = it[ReviewTable.reviewId]
                    val reviewImages = ReviewImageTable.select { ReviewImageTable.reviewId eq reviewId }
                        .map { it[ReviewImageTable.reviewId] to it[ReviewImageTable.image] }

                    it.toReviewResponse(reviewImages)
                }

            val commentsCount = CommentTable.join(UserTable, JoinType.INNER)
                .select { CommentTable.recipeId.eq(recipeId) }.map { it[CommentTable.commentId] }.size

            getBaseQuery(
                RecipeTable.video, RecipeTable.description, RecipeTable.startEstimation,
                RecipeTable.endEstimation, RecipeTable.estimationUnit, Count(ReviewTable.rating)
            ).select { RecipeTable.recipeId eq recipeId }.getBaseGroupBy()
                .map { it.toRecipeDetailResponse(isFavorite, ingredients, tools, steps, nutrition, reviews, commentsCount) }
                .first()
        }
    }
}