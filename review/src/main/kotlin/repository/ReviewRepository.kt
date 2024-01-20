package repository

import model.review.ReviewRequest
import model.review.ReviewResponse

interface ReviewRepository {
    suspend fun insertReview(uid: String, historyId: String, request: ReviewRequest, fileBytes: List<ByteArray>)
    suspend fun getReviewByHistoryId(historyId: String): ReviewResponse?
}