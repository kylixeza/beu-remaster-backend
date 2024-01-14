package repository.category

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import database.DatabaseFactory
import model.category.CategoryRecipeRequest
import model.category.CategoryRequest
import model.category.CategoryResponse
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import tables.CategoryRecipeTable
import tables.CategoryTable
import tables.RecipeTable
import util.toCategoryResponse

class CategoryRepositoryImpl(
    private val db: DatabaseFactory
): CategoryRepository {
    override suspend fun insertCategory(request: CategoryRequest) {
        db.dbQuery {
            val id = "CATEGORY-${NanoIdUtils.randomNanoId()}"
            CategoryTable.insert {
                it[categoryId] = id
                it[name] = request.name
            }
        }
    }

    override suspend fun insertCategoryRecipe(request: CategoryRecipeRequest) {
        db.dbQuery {
            val categoryId = CategoryTable.select {
                CategoryTable.name eq request.categoryName
            }.first()[CategoryTable.categoryId]

            request.recipeName.forEach {
                val recipeId = RecipeTable.select {
                    RecipeTable.name eq it
                }.first()[RecipeTable.recipeId]

                CategoryRecipeTable.insert {
                    it[CategoryRecipeTable.categoryId] = categoryId
                    it[CategoryRecipeTable.recipeId] = recipeId
                }
            }
        }
    }

    override suspend fun getAllCategories(): List<CategoryResponse> {
        return db.dbQuery {
            CategoryTable.selectAll().map { it.toCategoryResponse()}
        }
    }
}