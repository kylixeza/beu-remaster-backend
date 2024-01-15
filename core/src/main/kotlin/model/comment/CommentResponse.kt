package model.comment

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @field:SerializedName("comment_id")
    val commentId: String,
    val avatar: String,
    val username: String,
    val comment: String,
    val time: String,

    val replies: List<ReplyCommentResponse>,
)