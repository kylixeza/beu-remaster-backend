package model.recipe

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RecipeListResponse(
    @field:SerializedName("recipe_id")
    val recipeId: String,
    val name: String,
    val difficulty: String,
    val image: String,
    val favorites: Long,
    val rating: BigDecimal,
    @field:SerializedName("estimation_time")
    val estimationTime: Int,
)
