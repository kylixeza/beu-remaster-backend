package repository

import model.comment.CommentRequest
import model.comment.CommentResponse

interface CommentRepository {
    suspend fun insertComment(request: CommentRequest, recipeId: String, uid: String)
    suspend fun getComments(uid: String, recipeId: String): List<CommentResponse>
}