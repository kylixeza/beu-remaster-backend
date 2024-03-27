package model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val username: String,
    val name: String,
    val avatar: String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
    val email: String?
)
