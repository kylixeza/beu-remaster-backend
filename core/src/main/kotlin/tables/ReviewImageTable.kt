package tables

import org.jetbrains.exposed.sql.Table

object ReviewImageTable: Table() {

    override val tableName: String = "review_image"

    val reviewId = reference("review_id", ReviewTable.reviewId)
    val image = varchar("image", 255)
}