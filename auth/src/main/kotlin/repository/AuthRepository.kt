package repository

import model.User
import model.auth.RegisterRequest
import security.hashing.SaltedHash

interface AuthRepository {
    suspend fun isEmailExist(email: String): Boolean
    suspend fun isPhoneNumberExist(phoneNumber: String): Boolean
    suspend fun isUsernameExist(username: String): Boolean
    suspend fun insertUser(body: RegisterRequest, saltedHash: SaltedHash): String
    suspend fun getUserByIdentifier(identifier: String): User?
}