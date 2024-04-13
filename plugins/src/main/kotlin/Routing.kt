import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import route.*
import route.category.CategoryRoute
import route.nutrition.NutritionRoute
import route.prediction.PredictionRoute
import route.recipe.RecipeRoute

fun Application.configureRouting() {
    val authRoute by inject<AuthRoute>()
    val categoryRoute by inject<CategoryRoute>()
    val nutritionRoute by inject<NutritionRoute>()
    val recipeRoute by inject<RecipeRoute>()
    val predictionRoute by inject<PredictionRoute>()
    val commentRoute by inject<CommentRoute>()
    val historyRoute by inject<HistoryRoute>()
    val reviewRoute by inject<ReviewRoute>()
    val favoriteRoute by inject<FavoriteRoute>()
    val profileRoute by inject<ProfileRoute>()
    val helpCenterRoute by inject<HelpCenterRoute>()
    val staticResourcesRoute by inject<StaticResourcesRoute>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoute.apply { auth() }
        categoryRoute.apply { categories() }
        nutritionRoute.apply { nutrition() }
        recipeRoute.apply { recipes(
            commentRoute,
            favoriteRoute
        )}
        predictionRoute.apply { prediction() }
        historyRoute.apply { histories(
            reviewRoute
        ) }

        favoriteRoute.apply { favorites() }
        profileRoute.apply { profile() }
        helpCenterRoute.apply { helpCenter() }
        staticResourcesRoute.apply { privacyPolicy(); termsAndConditions() }
    }
}
