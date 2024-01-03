package tables

import org.jetbrains.exposed.sql.Table

object IngredientTable: Table() {

    override val tableName: String = "ingredient"

    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val ingredient = varchar("ingredient", 128)
}