package repository

import database.DatabaseFactory
import database.getBaseQuery
import database.isFavorite
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
        isFavorite = true,
        favorites = this[Count(FavoriteTable.recipeId)],
        rating = this[Avg(ReviewTable.rating, 1)] ?: BigDecimal.ZERO,
        estimationTime = this[RecipeTable.endEstimation]
    )

    private fun Query.getBaseGroupBy() = groupBy(RecipeTable.recipeId, CategoryRecipeTable.categoryId, ReviewTable.reviewId)
}