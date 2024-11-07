package model.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val username: String,
    val name: String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
    val email: String
)