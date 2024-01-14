package di

import controller.category.CategoryController
import controller.category.CategoryControllerImpl
import controller.nutrition.NutritionController
import controller.nutrition.NutritionControllerImpl
import controller.recipe.RecipeController
import controller.recipe.RecipeControllerImpl
import org.koin.dsl.module
import repository.category.CategoryRepository
import repository.category.CategoryRepositoryImpl
import repository.nutrition.NutritionRepository
import repository.nutrition.NutritionRepositoryImpl
import repository.recipe.RecipeRepository
import repository.recipe.RecipeRepositoryImpl
import route.category.CategoryRoute
import route.nutrition.NutritionRoute
import route.recipe.RecipeRoute

val recipeModule = module {
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
    single<RecipeController> { RecipeControllerImpl(get()) }
    single { RecipeRoute(get(), get()) }
}

val categoryModule = module {
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<CategoryController> { CategoryControllerImpl(get()) }
    single { CategoryRoute(get(), get()) }
}

val nutritionModule = module {
    single<NutritionRepository> { NutritionRepositoryImpl(get()) }
    single<NutritionController> { NutritionControllerImpl(get()) }
    single { NutritionRoute(get()) }
}