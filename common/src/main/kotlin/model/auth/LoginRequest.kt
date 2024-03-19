package model.auth

data class LoginRequest(
    val identifier: String,
    val password: String
)
