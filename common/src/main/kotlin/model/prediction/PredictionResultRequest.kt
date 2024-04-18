package model.prediction

data class PredictionResultRequest(
    val prediction: String,
    val actual: String,
    val probability: Double,
)
