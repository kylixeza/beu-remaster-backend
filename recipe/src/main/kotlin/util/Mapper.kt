package util

import model.category.CategoryResponse
import model.nutrition.NutritionResponse
import model.recipe.RecipeDetailResponse
import model.recipe.RecipeListResponse
import model.review.ReviewResponse
import org.jetbrains.exposed.sql.Avg
import org.jetbrains.exposed.sql.Count
import org.jetbrains.exposed.sql.ResultRow
import tables.*
import java.math.BigDecimal

fun ResultRow.toCategoryResponse() = CategoryResponse(
    categoryId = this[CategoryTable.categoryId],
    name = this[CategoryTable.name]
)

fun ResultRow.toRecipeListResponse() = RecipeListResponse(
    recipeId = this[RecipeTable.recipeId],
    name = this[RecipeTable.name],
    difficulty = this[RecipeTable.difficulty].difficulty,
    image = this[RecipeTable.image],
    favorites = this[Count(FavoriteTable.recipeId)],
    rating = this[Avg(ReviewTable.rating, 1)] ?: BigDecimal.ZERO,
    estimationTime = this[RecipeTable.endEstimation]
)

fun ResultRow.toNutritionResponse() = NutritionResponse(
    nutritionId = this[NutritionTable.nutritionId],
    name = this[NutritionTable.name],
    amount = "${this[NutritionRecipeTable.amount]} ${this[NutritionTable.unit]}"
)

fun ResultRow.toReviewResponse(
    images: List<Pair<String, String>>
) = ReviewResponse(
    reviewId = this[ReviewTable.reviewId],
    username = this[UserTable.username],
    avatar = this[UserTable.avatar],
    rating = this[ReviewTable.rating],
    comment = this[ReviewTable.comment].orEmpty(),
    timeStamp = this[ReviewTable.timeStamp].breakDown(),
    images = images.filter { it.first == this[ReviewTable.reviewId] }.map { it.second }
)

fun ResultRow.toRecipeDetailResponse(
    isFavorite: Boolean,
    ingredients: List<String>,
    tools: List<String>,
    steps: List<String>,
    nutrition: List<NutritionResponse>,
    reviews: List<ReviewResponse>,
    commentsCount: Int,
) = RecipeDetailResponse(
    recipeId = this[RecipeTable.recipeId],
    isFavorite = isFavorite,
    name = this[RecipeTable.name],
    video = this[RecipeTable.video],
    ingredients = ingredients,
    tools = tools,
    steps = steps,
    averageRating = this[Avg(ReviewTable.rating, 1)] ?: BigDecimal.ZERO,
    averageCount = this[Count(ReviewTable.reviewId)],
    description = this[RecipeTable.description],
    estimateTime = "${this[RecipeTable.startEstimation]} - ${this[RecipeTable.endEstimation]} ${this[RecipeTable.estimationUnit]}",
    commentsCount = commentsCount,
    nutritionInformation = nutrition,
    reviews = reviews
)

fun String.enumerateRecipeDifficulty() = RecipeDifficulty.entries.first { it.difficulty == this }

fun String.enumeratePreferConsumeAt() = PreferConsumeAt.entries.first { it.translate == this }