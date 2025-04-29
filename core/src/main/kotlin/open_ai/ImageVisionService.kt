package open_ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ImageVisionService: OpenApiService {

    override suspend fun getChatResponse(request: Any): String {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val openAIApiKey = System.getenv("OPENAI_API_KEY")

        val response = client.post("https://api.openai.com/v1/chat/completions") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $openAIApiKey")
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            setBody(request)
        }
        val respondBody = response.body<ChatResponse>()
        client.close()
        return respondBody.choices.firstOrNull()?.message?.content ?: "No response"
    }
}