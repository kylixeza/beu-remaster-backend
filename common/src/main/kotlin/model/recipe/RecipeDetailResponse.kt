package model.recipe

import com.google.gson.annotations.SerializedName
import model.nutrition.NutritionResponse
import model.review.ReviewResponse
import java.math.BigDecimal

data class RecipeDetailResponse(
    @field:SerializedName("recipe_id")
    val recipeId: String,
    @field:SerializedName("is_favorite")
    val isFavorite: Boolean,
    val name: String,
    val video: String,
    @field:SerializedName("video_src")
    val videoSrc: String,
    val ingredients: List<String>,
    val tools: List<String>,
    val steps: List<String>,
    @field:SerializedName("average_rating")
    val averageRating: BigDecimal,
    @field:SerializedName("average_count")
    val averageCount: Long,
    val description: String,
    @field:SerializedName("estimate_time")
    val estimateTime: String,
    @field:SerializedName("comments_count")
    val commentsCount: Int,
    @field:SerializedName("nutrition_information")
    val nutritionInformation: List<NutritionResponse>,
    val reviews: List<ReviewResponse>
)
