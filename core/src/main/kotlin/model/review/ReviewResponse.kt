package model.review

data class ReviewResponse(
    val reviewId: String,
    val username: String,
    val rating: Int,
    val comment: String,
)
