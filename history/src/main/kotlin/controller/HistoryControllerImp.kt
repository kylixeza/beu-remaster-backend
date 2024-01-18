package controller

import base.buildErrorResponse
import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import model.history.HistoryRequest
import repository.HistoryRepository

class HistoryControllerImp(
    private val repository: HistoryRepository
): HistoryController {
    override suspend fun ApplicationCall.insertHistory(uid: String) {
        val request = try {
            receive<HistoryRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }
        buildSuccessResponse { repository.insertHistory(uid, request) }
    }

    override suspend fun ApplicationCall.getHistories(uid: String) {
        buildSuccessListResponse { repository.getHistories(uid) }
    }
}