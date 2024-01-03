package tables

import org.jetbrains.exposed.sql.Table

object NutritionRecipeTable: Table() {

    override val tableName: String = "nutrition_recipe"

    val nutritionId = reference("nutrition_id", NutritionTable.nutritionId)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val amount = integer("amount").default(0)
}