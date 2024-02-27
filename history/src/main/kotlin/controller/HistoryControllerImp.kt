package controller

import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.history.HistoryRequest
import repository.HistoryRepository

class HistoryControllerImp(
    private val repository: HistoryRepository
): HistoryController {
    override suspend fun ApplicationCall.insertHistory(uid: String) {
        val request = receive<HistoryRequest>()
        buildSuccessResponse { repository.insertHistory(uid, request) }
    }

    override suspend fun ApplicationCall.getHistories(uid: String) {
        buildSuccessListResponse { repository.getHistories(uid) }
    }
}