package controller.prediction

import base.buildSuccessListResponse
import base.buildSuccessResponse
import com.google.gson.Gson
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.prediction.PredictionResponse
import model.prediction.PredictionResultRequest
import repository.prediction.PredictionRepository

class PredictionControllerImpl(
    private val repository: PredictionRepository
): PredictionController {
    override suspend fun ApplicationCall.insertPredictionResult() {
        val multipart = receiveMultipart()
        var body: PredictionResultRequest? = null
        var fileByte: ByteArray? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "body") {
                        Gson().fromJson(part.value, PredictionResultRequest::class.java).let { body = it }
                    }
                }
                is PartData.FileItem -> {
                    if (part.name == "image") {
                        fileByte = part.streamProvider().readBytes()
                    }
                }
                else -> {}
            }
            part.dispose()
        }

        if (body !== null && fileByte != null) {
            repository.insertPredictionResult(body ?: return, fileByte ?: return)
            buildSuccessResponse { "Prediction result was added successfully" }
        }
    }

    override suspend fun ApplicationCall.getRelatedRecipes(uid: String, query: String) {
        buildSuccessListResponse { repository.getRelatedRecipes(uid, query) }
    }

    override suspend fun ApplicationCall.classifyImage(uid: String) {
        val multipart = receiveMultipart()
        var fileByte: ByteArray? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    fileByte = part.streamProvider().readBytes()
                }
                else -> {}
            }
            part.dispose()
        }

        if (fileByte != null) {
            val result = repository.classifyImage(uid, fileByte ?: return)
            val relatedRecipes = repository.getRelatedRecipes(uid, result)
            val response = PredictionResponse(
                classifiedImage = result,
                isFood = result != "Not A Food",
                relatedRecipes = relatedRecipes
            )
            buildSuccessResponse { response }
        }
    }
}