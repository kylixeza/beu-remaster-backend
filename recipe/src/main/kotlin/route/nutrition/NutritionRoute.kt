package route.nutrition

import controller.nutrition.NutritionController
import io.ktor.server.application.*
import io.ktor.server.routing.*

class NutritionRoute(
    private val nutritionController: NutritionController,
) {

    fun Route.nutrition() {
        route("/nutrition") {

            post {
                nutritionController.apply { call.insertNutrition() }
            }

            post("/recipe") {
                nutritionController.apply { call.insertNutritionRecipe() }
            }
        }
    }

}