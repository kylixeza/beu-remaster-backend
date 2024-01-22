package repository

import model.user.UserRequest
import model.user.UserResponse
import security.hashing.SaltedHash

interface ProfileRepository {
    suspend fun getUser(uid: String): UserResponse
    suspend fun updateUser(uid: String, request: UserRequest, fileByte: ByteArray?): UserResponse
    suspend fun resetPassword(uid: String, saltedHash: SaltedHash)
    suspend fun isUsernameExist(uid: String, username: String): Boolean
    suspend fun isEmailExist(uid: String, email: String): Boolean
    suspend fun isPhoneNumberExist(uid: String, phoneNumber: String): Boolean
}