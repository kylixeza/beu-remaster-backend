package route.category

import Middleware
import controller.category.CategoryController
import io.ktor.server.application.*
import io.ktor.server.routing.*

class CategoryRoute(
    private val categoryController: CategoryController,
    private val middleware: Middleware
) {

    fun Route.categories() {
        route("/categories") {

            post {
                categoryController.apply { call.insertCategory()  }
            }

            post("/recipe") {
                categoryController.apply { call.insertCategoryRecipe() }
            }

            middleware.apply {
                authenticate(HTTPVerb.GET) { _, call ->
                    categoryController.apply { call.getCategories() }
                }
            }
        }
    }

}