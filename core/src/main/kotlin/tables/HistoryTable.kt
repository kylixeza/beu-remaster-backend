package tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object HistoryTable: Table() {

    override val tableName: String = "history"

    val historyId = varchar("history_id", 255)
    val userId = reference("user_id", UserTable.uid)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val spendTime = integer("spend_time")
    val timeStamp = datetime("created_at")

    override val primaryKey = PrimaryKey(historyId)
}