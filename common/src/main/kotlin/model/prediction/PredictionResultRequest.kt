package model.prediction

data class PredictionResultRequest(
    val expected: String,
    val predicted: String,
    val probability: Double,
)
