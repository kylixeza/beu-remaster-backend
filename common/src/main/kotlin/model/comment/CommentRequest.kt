package model.comment

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    val comment: String,
    @field:SerializedName("reply_comment_id")
    val replyCommentId: String?,
)
