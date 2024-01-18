package repository

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.history.HistoryRequest
import model.history.HistoryResponse
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.HistoryTable
import tables.RecipeTable
import tables.ReviewTable
import util.breakDown
import util.createTimeStamp
import util.durationSince

class HistoryRepositoryImpl(
    private val db: DatabaseFactory
): HistoryRepository {
    override suspend fun insertHistory(uid: String, request: HistoryRequest): String {
        return db.dbQuery {
            val historyId = "HISTORY-${NanoIdUtils.randomNanoId()}"

            HistoryTable.insert {
                it[this.historyId] = historyId
                it[this.uid] = uid
                it[recipeId] = request.recipeId
                it[spendTime] = request.spendTime
                it[timeStamp] = createTimeStamp()
            }

            historyId
        }
    }

    override suspend fun getHistories(uid: String): List<HistoryResponse> {
        return db.dbQuery {
            HistoryTable.join(RecipeTable, JoinType.INNER) {
                HistoryTable.recipeId eq RecipeTable.recipeId
            }.select {
                HistoryTable.uid eq uid
            }.map {
                val isReviewed = ReviewTable.select {
                    ReviewTable.historyId eq it[HistoryTable.historyId]
                }.count() > 0

                val reviewRating = if (isReviewed) {
                    ReviewTable.select {
                        ReviewTable.historyId eq it[HistoryTable.historyId]
                    }.map {
                        it[ReviewTable.rating]
                    }.first()
                } else {
                    0
                }

                it.toHistoryResponse(isReviewed, reviewRating)
            }
        }
    }


    private fun ResultRow.toHistoryResponse(
        isReviewed: Boolean,
        reviewRating: Int
    ) = HistoryResponse(
        historyId = this[HistoryTable.historyId],
        recipeName = this[RecipeTable.name],
        recipeImage = this[RecipeTable.image],
        spendTime = this[HistoryTable.spendTime].toPossibleTime(),
        timeStamp = this[HistoryTable.timeStamp].breakDown(),
        isReviewed = isReviewed,
        reviewRating = reviewRating
    )

    private fun Int.toPossibleTime(): String {
        val seconds = this % 60
        val minutes = (this / 60)

        return "$minutes menit $seconds detik"
    }
}
