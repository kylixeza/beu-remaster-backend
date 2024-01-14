package model.nutrition

import com.google.gson.annotations.SerializedName

data class NutritionRecipeRequest(
    @field:SerializedName("nutrition_name")
    val nutritionName: String,
    val recipes: Map<String, Int>
)
