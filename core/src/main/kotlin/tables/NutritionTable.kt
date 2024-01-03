package tables

import org.jetbrains.exposed.sql.Table

object NutritionTable: Table() {

    override val tableName: String = "nutrition"

    val nutritionId = varchar("nutrition_id", 128)
    val name = varchar("name", 128)

}