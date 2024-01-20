package repository

import database.DatabaseFactory
import model.recipe.RecipeListResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import tables.CategoryRecipeTable
import tables.FavoriteTable
import tables.RecipeTable
import tables.ReviewTable
import java.math.BigDecimal

class FavoriteRepositoryImpl(
    private val db: DatabaseFactory
): FavoriteRepository {

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

    override suspend fun insertFavorite(uid: String, recipeId: String) {
        db.dbQuery {
            FavoriteTable.insert {
                it[this.uid] = uid
                it[this.recipeId] = recipeId
            }
        }
    }

    override suspend fun deleteFavorite(uid: String, recipeId: String) {
        db.dbQuery {
            FavoriteTable.deleteWhere {
                (FavoriteTable.uid eq uid) and (FavoriteTable.recipeId eq recipeId)
            }
        }
    }

    override suspend fun getFavorites(uid: String): List<RecipeListResponse> {
        return db.dbQuery {
            getBaseQuery().select {
                FavoriteTable.uid eq uid
            }.getBaseGroupBy().map {
                it.toRecipeListResponse()
            }
        }
    }

    private fun ResultRow.toRecipeListResponse() = RecipeListResponse(
        recipeId = this[RecipeTable.recipeId],
        name = this[RecipeTable.name],
        difficulty = this[RecipeTable.difficulty].difficulty,
        image = this[RecipeTable.image],
        favorites = this[Count(FavoriteTable.recipeId)],
        rating = this[Avg(ReviewTable.rating, 1)] ?: BigDecimal.ZERO,
        estimationTime = this[RecipeTable.endEstimation]
    )

    private fun Query.getBaseGroupBy() = groupBy(RecipeTable.recipeId, CategoryRecipeTable.categoryId, ReviewTable.reviewId)
}