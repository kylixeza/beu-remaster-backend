package database

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tables.*

class DatabaseFactory(
	dataSource: HikariDataSource
) {

	init {
		Database.connect(dataSource)
		transaction {
			val tables = listOf(
				UserTable, TokenBlacklistTable, RecipeTable, CategoryTable, CategoryRecipeTable, NutritionTable,
				NutritionRecipeTable, StepTable, IngredientTable, ToolTable, CommentTable, ReviewTable,
				ReviewImageTable, HistoryTable, FavoriteTable, HelpCenterTable, PredictionResultTable
			)
			tables.forEach { table ->
				SchemaUtils.create(table)
				SchemaUtils.createMissingTablesAndColumns(table)
				SchemaUtils.addMissingColumnsStatements(table)
			}
		}
	}
	
	suspend fun <T> dbQuery(block: () -> T): T =
		withContext(Dispatchers.IO) {
			transaction { block() }
		}
}