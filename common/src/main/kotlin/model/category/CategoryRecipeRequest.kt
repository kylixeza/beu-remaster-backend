package model.category

data class CategoryRecipeRequest(
    val categoryName: String,
    val recipeName: List<String>,
)
