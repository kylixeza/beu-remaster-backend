package tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ReviewTable: Table() {

    override val tableName: String = "review"

    val reviewId = varchar("review_id", 128)
    val uid = reference("uid", UserTable.uid)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val rating = integer("rating").default(0)
    val comment = varchar("comment", 255).nullable()
    val timeStamp = datetime("time_stamp")
}