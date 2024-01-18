package model.review

data class ReviewRequest(
    val rating: Int,
    val comment: String,
)
