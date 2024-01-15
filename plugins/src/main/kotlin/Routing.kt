import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import route.AuthRoute
import route.CommentRoute
import route.category.CategoryRoute
import route.nutrition.NutritionRoute
import route.recipe.RecipeRoute

fun Application.configureRouting() {
    val authRoute by inject<AuthRoute>()
    val categoryRoute by inject<CategoryRoute>()
    val nutritionRoute by inject<NutritionRoute>()
    val recipeRoute by inject<RecipeRoute>()
    val commentRoute by inject<CommentRoute>()


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoute.apply { initRoutes() }
        categoryRoute.apply { categories() }
        nutritionRoute.apply { nutrition() }
        recipeRoute.apply { recipes(
            commentRoute
        )}
    }
}
