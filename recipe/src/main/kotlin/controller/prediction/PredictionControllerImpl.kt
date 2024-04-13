package controller.prediction

import base.buildSuccessListResponse
import base.buildSuccessResponse
import com.google.gson.Gson
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
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
            buildSuccessResponse { "Hasil Prediksi Berhasil Ditambahkan" }
        }
    }

    override suspend fun ApplicationCall.getRelatedRecipes(uid: String, query: String) {
        buildSuccessListResponse { repository.getRelatedRecipes(uid, query) }
    }
}