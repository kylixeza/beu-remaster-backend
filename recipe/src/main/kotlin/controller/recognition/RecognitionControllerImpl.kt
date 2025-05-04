package controller.recognition

import base.buildSuccessListResponse
import base.buildSuccessResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.prediction.PredictionResponse
import model.prediction.PredictionResultRequest
import repository.recognition.RecognitionRepository

class RecognitionControllerImpl(
    private val repository: RecognitionRepository
): RecognitionController {
    override suspend fun ApplicationCall.insertRecognitionResult() {
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
            repository.insertRecognitionResult(body ?: return, fileByte ?: return)
            buildSuccessResponse { "Prediction result was added successfully" }
        }
    }

    @Deprecated("Use recognizeImage instead")
    override suspend fun ApplicationCall.getRelatedRecipes(uid: String, query: String) {
        buildSuccessListResponse { repository.getRelatedRecipes(uid, query, null) }
    }

    override suspend fun ApplicationCall.recognizeImage(uid: String) {
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
            val result = repository.recognizeImage(uid, fileByte ?: return)

            val mapType = object : TypeToken<Map<String, String>>() {}.type
            val recognitionResult = Gson().fromJson<Map<String, String>>(result, mapType)
            val recognizedImage = recognitionResult["result"] ?: "Not A Food"
            val closestCategory = recognitionResult["closest_category"] ?: "Not A Food"

            val relatedRecipes = repository.getRelatedRecipes(uid, recognizedImage, closestCategory)
            val response = PredictionResponse(
                classifiedImage = recognizedImage,
                isFood = recognizedImage != "Not A Food",
                relatedRecipes = relatedRecipes
            )
            buildSuccessResponse { response }
        }
    }
}