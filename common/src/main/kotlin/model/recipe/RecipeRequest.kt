package model.recipe

import com.google.gson.annotations.SerializedName

data class RecipeRequest(
    val name: String,
    val description: String,
    val difficulty: String,
    val image: String,
    @field:SerializedName("start_estimation")
    val startEstimation: Int,
    @field:SerializedName("end_estimation")
    val endEstimation: Int,
    @field:SerializedName("estimation_unit")
    val estimationUnit: String,
    val video: String,
    @field:SerializedName("video_src")
    val videoSrc: String,
    @field:SerializedName("prefer_consume_at")
    val preferConsumeAt: String,
    val ingredients: List<String>,
    val tools: List<String>,
    val steps: Map<Int, String>,
    val nutrition: Map<String, Int>,
    val categories: List<String>?,
)
