package controller

import io.ktor.server.application.*

interface HistoryController {
    suspend fun ApplicationCall.insertHistory(uid: String)
    suspend fun ApplicationCall.getHistories(uid: String)
}