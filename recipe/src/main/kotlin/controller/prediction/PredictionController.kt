package controller.prediction

import io.ktor.server.application.*

interface PredictionController {
    suspend fun ApplicationCall.insertPredictionResult()
    suspend fun ApplicationCall.getRelatedRecipes(uid: String, query: String)
}