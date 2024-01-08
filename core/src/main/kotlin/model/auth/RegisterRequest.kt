package model.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
    val name: String,
)
