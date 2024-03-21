package repository

import database.*
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
            val recipeIds = FavoriteTable.select {
                FavoriteTable.uid eq uid
            }.map { it[FavoriteTable.recipeId] }

            getBaseQuery().select {
                RecipeTable.recipeId inList recipeIds
            }.getBaseGroupBy().map {
                it.toRecipeListResponse(uid)
            }
        }
    }

    private fun ResultRow.toRecipeListResponse(uid: String) = RecipeListResponse(
        recipeId = this[RecipeTable.recipeId],
        name = this[RecipeTable.name],
        difficulty = this[RecipeTable.difficulty].difficulty,
        image = this[RecipeTable.image],
        isFavorite = this.isFavorite(uid),
        favorites = favoritesCount(this[RecipeTable.recipeId]),
        rating = this[Avg(ReviewTable.rating, 1)] ?: BigDecimal.ZERO,
        estimationTime = this[RecipeTable.endEstimation]
    )
}