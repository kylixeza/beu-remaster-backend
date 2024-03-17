package database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import tables.CategoryRecipeTable
import tables.FavoriteTable
import tables.RecipeTable
import tables.ReviewTable

fun getBaseQuery(vararg additionalColumn: Expression<*> = emptyArray()): FieldSet {
    val baseColumns = listOf(
        RecipeTable.recipeId, RecipeTable.name, RecipeTable.difficulty, RecipeTable.image,
        Avg(ReviewTable.rating, 1), RecipeTable.endEstimation,
        CategoryRecipeTable.categoryId,
    )
    return RecipeTable.join(ReviewTable, JoinType.LEFT) {
        RecipeTable.recipeId eq ReviewTable.recipeId
    }.join(CategoryRecipeTable, JoinType.INNER) {
        RecipeTable.recipeId eq CategoryRecipeTable.recipeId
    }.slice(
        columns = if (additionalColumn.isNotEmpty()) baseColumns + additionalColumn else baseColumns
    )
}

fun Query.getBaseGroupBy() = groupBy(RecipeTable.recipeId, CategoryRecipeTable.categoryId)

fun ResultRow.isFavorite(uid: String) = FavoriteTable.select {
    val recipeId = this@isFavorite[RecipeTable.recipeId]
    FavoriteTable.recipeId.eq(recipeId) and FavoriteTable.uid.eq(uid)
}.map { it[FavoriteTable.recipeId] }.isNotEmpty()

fun favoritesCount(recipeId: String) = FavoriteTable.select {
    FavoriteTable.recipeId.eq(recipeId)
}.count()