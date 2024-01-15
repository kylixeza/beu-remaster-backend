package tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CommentTable: Table() {

    override val tableName: String = "comment"

    val commentId = varchar("comment_id", 128)
    val replyCommentId = reference("reply_comment_id", commentId).nullable()
    val uid = reference("uid", UserTable.uid)
    val recipeId = reference("recipe_id", RecipeTable.recipeId)
    val comment = varchar("comment", 255)
    val timeStamp = datetime("time_stamp")

    override val primaryKey: PrimaryKey? = PrimaryKey(commentId)
}