package model.auth

data class RegisterRequest(
    val password: String,
    val username: String,
    val email: String,
)