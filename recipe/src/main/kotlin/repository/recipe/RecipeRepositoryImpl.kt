package repository.recipe

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.recipe.RecipeDetailResponse
import model.recipe.RecipeListResponse
import model.recipe.RecipeRequest
import org.jetbrains.exposed.sql.*
import tables.*
import util.*

class RecipeRepositoryImpl(
    private val db: DatabaseFactory
): RecipeRepository {

    private fun getBaseQuery(vararg additionalColumn: Expression<*> = emptyArray()): FieldSet {
        val baseColumns = listOf(
            RecipeTable.recipeId, RecipeTable.name, RecipeTable.difficulty, RecipeTable.image,
            Count(FavoriteTable.recipeId), Avg(ReviewTable.rating, 1), RecipeTable.endEstimation,
            CategoryRecipeTable.categoryId, ReviewTable.uid, ReviewTable.reviewId
        )
        return RecipeTable.join(FavoriteTable, JoinType.LEFT) {
            RecipeTable.recipeId eq FavoriteTable.recipeId
        }.join(ReviewTable, JoinType.LEFT) {
            RecipeTable.recipeId eq ReviewTable.recipeId
        }.join(CategoryRecipeTable, JoinType.INNER) {
            RecipeTable.recipeId eq CategoryRecipeTable.recipeId
        }.slice(
            columns = if (additionalColumn.isNotEmpty()) baseColumns + additionalColumn else baseColumns
        )
    }

    private fun Query.getBaseGroupBy() = groupBy(RecipeTable.recipeId, CategoryRecipeTable.categoryId, ReviewTable.reviewId)

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

    override suspend fun getPreferredRecipesByConsumeTime(): List<RecipeListResponse> = db.dbQuery {
        val preferAt = getPreferConsumeAt()
        getBaseQuery().select {
            RecipeTable.preferConsumeAt.eq(preferAt)
        }.getBaseGroupBy().map { it.toRecipeListResponse() }
    }

    override suspend fun getHealthyRecipes(): List<RecipeListResponse> {
        val categoryName = "Sayur"
        return db.dbQuery {
            val recipeIdByCategoryName = CategoryTable.join(CategoryRecipeTable, JoinType.INNER) {
                CategoryTable.categoryId eq CategoryRecipeTable.categoryId
            }.select { CategoryTable.name eq categoryName }.map { it[CategoryRecipeTable.categoryId] }
            getBaseQuery().selectAll()
                .getBaseGroupBy()
                .map { it.toRecipeListResponse() }
                .filter { it.recipeId in recipeIdByCategoryName }
        }
    }

    override suspend fun getBestRecipes(): List<RecipeListResponse> {
        return db.dbQuery {
            getBaseQuery().selectAll()
                .getBaseGroupBy()
                .orderBy(Avg(ReviewTable.rating, 1), SortOrder.DESC)
                .map { it.toRecipeListResponse() }
        }
    }

    override suspend fun getRecipesByCategory(categoryId: String): List<RecipeListResponse> {
        return db.dbQuery {
            getBaseQuery()
                .select { CategoryRecipeTable.categoryId eq categoryId }
                .getBaseGroupBy()
                .map { it.toRecipeListResponse() }
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

            val reviewImages = ReviewImageTable.select { ReviewImageTable.reviewId eq ReviewTable.reviewId }
                .map { it[ReviewTable.reviewId] to it[ReviewImageTable.image] }

            val reviews = ReviewTable.join(UserTable, JoinType.INNER)
                .select { ReviewTable.recipeId.eq(recipeId) }
                .mapNotNull { it.toReviewResponse(reviewImages) }

            val commentsCount = CommentTable.join(UserTable, JoinType.INNER)
                .select { CommentTable.recipeId.eq(recipeId) }.map { it[CommentTable.commentId] }.size

            getBaseQuery(
                RecipeTable.video, RecipeTable.description, RecipeTable.startEstimation,
                RecipeTable.endEstimation, RecipeTable.estimationUnit, Count(ReviewTable.reviewId)
            ).selectAll().getBaseGroupBy()
                .map { it.toRecipeDetailResponse(isFavorite, ingredients, tools, steps, nutrition, reviews, commentsCount) }
                .first()
        }
    }
}