package controller.recognition

import io.ktor.server.application.*

interface RecognitionController {
    suspend fun ApplicationCall.insertRecognitionResult()
    suspend fun ApplicationCall.getRelatedRecipes(uid: String, query: String)
    suspend fun ApplicationCall.recognizeImage(uid: String)
}