package open_ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val choices: List<ChatChoiceResponse>
)

@Serializable
data class ChatChoiceResponse(
    val index: Int,
    val message: ChatMessageResponse
)

@Serializable
data class ChatMessageResponse(
    val role: String,
    val content: String
)