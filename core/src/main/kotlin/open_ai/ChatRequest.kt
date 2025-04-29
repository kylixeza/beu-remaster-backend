package open_ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageContentImageUrl(
    val url: String,
    val detail: String,
)

@Serializable
data class ChatMessageContent(
    val type: String,
    val text: String? = null,
    @SerialName("image_url")
    val imageUrl: ChatMessageContentImageUrl? = null
)

@Serializable
data class ChatMessage(
    val role: String,
    val content: List<ChatMessageContent>
)

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    @SerialName("max_tokens")
    val maxTokens: Int
)