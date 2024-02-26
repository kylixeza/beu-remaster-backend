package model.comment

abstract class BaseCommentResponse {
    abstract val commentId: String
    abstract val avatar: String
    abstract val username: String
    abstract val comment: String
    abstract val time: String
}