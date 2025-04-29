package open_ai

interface OpenApiService {
    suspend fun getChatResponse(request: Any): String
}