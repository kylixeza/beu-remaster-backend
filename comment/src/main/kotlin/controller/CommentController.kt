package controller

import io.ktor.server.application.*

interface CommentController {

    suspend fun ApplicationCall.insertComment(recipeId: String, uid: String)
    suspend fun ApplicationCall.getComments(recipeId: String)

}