package repository

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.review.ReviewRequest
import model.review.ReviewResponse
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import storage.CloudStorageService
import tables.HistoryTable
import tables.ReviewImageTable
import tables.ReviewTable
import tables.UserTable
import util.breakDown
import util.createTimeStamp

class ReviewRepositoryImpl(
    private val db: DatabaseFactory,
    private val cloudStorageService: CloudStorageService
): ReviewRepository {
    override suspend fun insertReview(
        uid: String,
        historyId: String,
        request: ReviewRequest,
        fileBytes: List<ByteArray>
    ) {
        db.dbQuery {
            val recipeId = HistoryTable
                .select { HistoryTable.historyId eq historyId }
                .map { it[HistoryTable.recipeId] }.first()

            val reviewId = "REVIEW-${NanoIdUtils.randomNanoId()}"
            ReviewTable.insert {
                it[this.reviewId] = reviewId
                it[this.historyId] = historyId
                it[this.uid] = uid
                it[this.recipeId] = recipeId
                it[this.rating] = request.rating
                it[this.comment] = request.comment
                it[this.timeStamp] = createTimeStamp()
            }

            fileBytes.map { fileByte ->
                val url = cloudStorageService.run { fileByte.uploadFile("reviews/$reviewId/") }

                ReviewImageTable.insert {
                    it[this.reviewId] = reviewId
                    it[this.image] = url
                }
            }
        }
    }

    override suspend fun getReviewByHistoryId(historyId: String): ReviewResponse? {
        return db.dbQuery {

            val reviewId = HistoryTable.join(ReviewTable, JoinType.LEFT) {
                HistoryTable.historyId eq ReviewTable.historyId
            }.select { HistoryTable.historyId eq historyId }.map { it[ReviewTable.reviewId] }.firstOrNull()

            if (reviewId == null) return@dbQuery null
            else {
                val images = ReviewImageTable
                    .select { ReviewImageTable.reviewId eq reviewId }
                    .map { it[ReviewImageTable.reviewId] to it[ReviewImageTable.image] }

                ReviewTable.join(UserTable, JoinType.INNER) {
                    ReviewTable.uid eq UserTable.uid
                }.select { ReviewTable.reviewId eq reviewId }
                    .map { it.toReviewResponse(images) }.firstOrNull()
            }

        }
    }

    private fun ResultRow.toReviewResponse(
        images: List<Pair<String, String>>
    ) = ReviewResponse(
        reviewId = this[ReviewTable.reviewId],
        username = this[UserTable.username],
        avatar = this[UserTable.avatar],
        rating = this[ReviewTable.rating],
        comment = this[ReviewTable.comment].orEmpty(),
        timeStamp = this[ReviewTable.timeStamp].breakDown(),
        images = images.filter { it.first == this[ReviewTable.reviewId] }.map { it.second }
    )
}