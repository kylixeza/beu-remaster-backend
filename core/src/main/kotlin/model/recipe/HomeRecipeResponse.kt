package model.recipe

import com.google.gson.annotations.SerializedName

data class HomeRecipeResponse(
    @field:SerializedName("preferred_recipe")
    val preferredRecipe: BaseHomeRecipeResponse,
    @field:SerializedName("healthy_recipe")
    val healthyRecipe: BaseHomeRecipeResponse,
    @field:SerializedName("best_recipe")
    val bestRecipe: BaseHomeRecipeResponse
)

data class BaseHomeRecipeResponse(
    val title: String,
    val subtitle: String?,
    val recipes: List<RecipeListResponse>
)
