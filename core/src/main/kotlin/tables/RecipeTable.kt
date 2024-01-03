package tables

import org.jetbrains.exposed.sql.Table

object RecipeTable: Table() {

    override val tableName: String = "recipe"

    val recipeId = varchar("recipe_id", 256)
    val name = varchar("name", 128).default("")
    val description = varchar("description", 2048).default("")
    val difficulty = integer("difficulty").default(0)
    val image = varchar("image", 512).default("")
    val startEstimation = integer("start_estimation").default(0)
    val endEstimation = integer("end_estimation").default(0)
    val estimationUnit = varchar("estimation_unit", 64).default("")
    val video = varchar("video", 512).default("")

    override val primaryKey: PrimaryKey = PrimaryKey(recipeId)
}