package model.history

import com.google.gson.annotations.SerializedName

data class HistoryRequest(
    @field:SerializedName("recipe_id")
    val recipeId: String,
    @field:SerializedName("spend_time")
    val spendTime: Int,
)
