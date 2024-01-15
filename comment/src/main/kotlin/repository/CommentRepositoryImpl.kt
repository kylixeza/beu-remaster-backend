package repository

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.comment.CommentRequest
import model.comment.CommentResponse
import model.comment.ReplyCommentResponse
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.CommentTable
import tables.UserTable
import util.createTimeStamp
import util.durationSince

class CommentRepositoryImpl(
    private val db: DatabaseFactory
): CommentRepository {

    override suspend fun insertComment(request: CommentRequest, recipeId: String, uid: String) {
        db.dbQuery {

            val commentId = "COMMENT-${NanoIdUtils.randomNanoId()}"
            val timeStamp = createTimeStamp()

            CommentTable.insert {
                it[this.commentId] = commentId
                it[replyCommentId] = request.replyCommentId
                it[this.uid] = uid
                it[this.recipeId] = recipeId
                it[comment] = request.comment
                it[this.timeStamp] = timeStamp
            }
        }
    }

    override suspend fun getComments(recipeId: String): List<CommentResponse> = db.dbQuery {

        val replies = CommentTable.join(UserTable, JoinType.INNER) {
            CommentTable.uid eq UserTable.uid
        }.select { (CommentTable.replyCommentId.isNotNull()) and (CommentTable.recipeId eq recipeId) }
            .map { ReplyCommentResponse(
                commentId = it[CommentTable.commentId],
                avatar = it[UserTable.avatar],
                username = it[UserTable.username],
                comment = it[CommentTable.comment],
                time = it[CommentTable.timeStamp].durationSince(),
                replyCommentId = it[CommentTable.replyCommentId].orEmpty()
            ) }

        CommentTable.join(UserTable, JoinType.INNER) {
            CommentTable.uid eq UserTable.uid
        }.select { CommentTable.replyCommentId.isNull() and (CommentTable.recipeId eq recipeId) }
            .map {
                CommentResponse(
                    commentId = it[CommentTable.commentId],
                    avatar = it[UserTable.avatar],
                    username = it[UserTable.username],
                    comment = it[CommentTable.comment],
                    time = it[CommentTable.timeStamp].durationSince(),
                    replies = replies.filter { reply -> reply.replyCommentId == it[CommentTable.commentId] }
                )
            }
    }

}