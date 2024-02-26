package model.recipe

data class HomeRecipeResponse(
    val title: String,
    val subtitle: String?,
    val recipes: List<model.recipe.RecipeListResponse>
)
