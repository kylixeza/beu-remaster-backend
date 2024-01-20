package controller

import base.buildSuccessResponse
import com.google.gson.Gson
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.review.ReviewRequest
import repository.ReviewRepository

class ReviewControllerImpl(
    private val repository: ReviewRepository
): ReviewController {
    override suspend fun ApplicationCall.insertReview(uid: String, historyId: String) {
        val multipart = receiveMultipart()
        var body: ReviewRequest? = null
        val fileBytes = mutableListOf<ByteArray>()

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "body") {
                        Gson().fromJson(part.value, ReviewRequest::class.java).let { body = it }
                    }
                }
                is PartData.FileItem -> {
                    if (part.name == "image") {
                        fileBytes.add(part.streamProvider().readBytes())
                    }
                }
                else -> {}
            }
            part.dispose()
        }

        if (body != null) {
            repository.insertReview(uid, historyId, body!!, fileBytes)
            buildSuccessResponse { "Ulasan berhasil ditambahkan" }
        }
    }

    override suspend fun ApplicationCall.getReviewByHistoryId(historyId: String) {
        buildSuccessResponse { repository.getReviewByHistoryId(historyId) }
    }
}