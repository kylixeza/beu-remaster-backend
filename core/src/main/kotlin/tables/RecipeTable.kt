package tables

import org.jetbrains.exposed.sql.Table
import util.PreferConsumeAt
import util.RecipeDifficulty

object RecipeTable: Table() {

    override val tableName: String = "recipe"

    val recipeId = varchar("recipe_id", 256)
    val name = varchar("name", 128).default("")
    val description = varchar("description", 2048).default("")
    val difficulty = enumeration("difficulty", RecipeDifficulty::class).default(RecipeDifficulty.EASY)
    val image = varchar("image", 512).default("")
    val startEstimation = integer("start_estimation").default(0)
    val endEstimation = integer("end_estimation").default(0)
    val estimationUnit = varchar("estimation_unit", 64).default("")
    val video = varchar("video", 512).default("")
    val videoSrc = varchar("video_src", 512).default("")
    val preferConsumeAt = enumeration("prefer_consume_at", PreferConsumeAt::class).default(PreferConsumeAt.BREAKFAST)

    override val primaryKey: PrimaryKey = PrimaryKey(recipeId)
}