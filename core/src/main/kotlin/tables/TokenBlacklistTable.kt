package tables

import org.jetbrains.exposed.sql.Table

object TokenBlacklistTable: Table() {

    override val tableName: String = "token_blacklist"

    val token = varchar("token", 255)
}