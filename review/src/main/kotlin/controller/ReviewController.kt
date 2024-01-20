package controller

import io.ktor.server.application.*

interface ReviewController {
    suspend fun ApplicationCall.insertReview(uid: String, historyId: String)
    suspend fun ApplicationCall.getReviewByHistoryId(historyId: String)
}