package database

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseFactory(
	dataSource: HikariDataSource
) {
	
	init {
		Database.connect(dataSource)
		transaction {
			val tables = listOf<Table>()
			tables.forEach { table ->
				SchemaUtils.create(table)
				SchemaUtils.createMissingTablesAndColumns(table)
			}
		}
	}
	
	suspend fun <T> dbQuery(block: () -> T): T =
		withContext(Dispatchers.IO) {
			transaction { block() }
		}
}