package tables

import org.jetbrains.exposed.sql.Table

object CategoryRecipeTable: Table() {

    override val tableName: String = "category_recipe"

    val categoryId = reference("category_id", CategoryTable.categoryId)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
}