package model.comment

import com.google.gson.annotations.SerializedName

data class ReplyCommentResponse(
    @field:SerializedName("comment_id")
    val commentId: String,
    val avatar: String,
    val username: String,
    val comment: String,
    val time: String,

    @field:SerializedName("reply_comment_id")
    val replyCommentId: String,
)
