package model.prediction

import com.google.gson.annotations.SerializedName
import model.recipe.RecipeListResponse

data class PredictionResponse(
    @SerializedName("classified_image")
    val classifiedImage: String,
    @SerializedName("is_food")
    val isFood: Boolean,
    @SerializedName("related_recipes")
    val relatedRecipes: List<RecipeListResponse>
)
