package tables

import org.jetbrains.exposed.sql.Table

object CategoryTable: Table() {

    override val tableName: String = "category"

    val categoryId = varchar("category_id", 256)
    val name = varchar("name", 32).default("")

    override val primaryKey: PrimaryKey = PrimaryKey(categoryId)
}