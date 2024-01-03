package tables

import org.jetbrains.exposed.sql.Table

object StepTable: Table() {

    override val tableName: String = "step"

    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val step = varchar("step", 128)
    val order = integer("order").default(0)
}