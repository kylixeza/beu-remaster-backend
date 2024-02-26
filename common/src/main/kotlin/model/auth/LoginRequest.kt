package model.auth

data class LoginRequest(
    val email: String,
    val password: String
)
