package tables

import org.jetbrains.exposed.sql.Table

object ToolTable: Table() {

    override val tableName: String = "tool"

    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val tool = varchar("tool", 128)
}