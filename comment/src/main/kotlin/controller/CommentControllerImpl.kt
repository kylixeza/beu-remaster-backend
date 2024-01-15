package controller

import base.buildErrorResponse
import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.comment.CommentRequest
import repository.CommentRepository

class CommentControllerImpl(
    private val repository: CommentRepository
): CommentController {
    override suspend fun ApplicationCall.insertComment(recipeId: String, uid: String) {
        val body = try {
            receive<CommentRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        buildSuccessResponse("Komentar berhasil ditambahkan") { repository.insertComment(body, recipeId, uid) }
    }

    override suspend fun ApplicationCall.getComments(recipeId: String) {
        buildSuccessListResponse { repository.getComments(recipeId) }
    }
}