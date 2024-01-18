package repository

import model.history.HistoryRequest
import model.history.HistoryResponse

interface HistoryRepository {

    suspend fun insertHistory(uid: String, request: HistoryRequest): String

    suspend fun getHistories(uid: String): List<HistoryResponse>

}