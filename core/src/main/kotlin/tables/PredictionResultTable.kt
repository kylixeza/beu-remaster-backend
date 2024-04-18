package tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PredictionResultTable: Table() {

    override val tableName: String
        get() = "prediction_result"

    val predictionId = varchar("prediction_id", 255)
    val timestamp = datetime("time_stamp")
    val prediction = varchar("prediction", 255)
    val actual = varchar("actual", 255)
    val probability = double("probability")
    val image = varchar("image", 255)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(predictionId)
}