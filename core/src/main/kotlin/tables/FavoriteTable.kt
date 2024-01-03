package tables

import org.jetbrains.exposed.sql.Table

object FavoriteTable: Table() {

    override val tableName: String = "favorite"

    val uid = reference("uid", UserTable.uid)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
}