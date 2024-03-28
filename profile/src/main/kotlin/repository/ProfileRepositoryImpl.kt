package repository

import database.DatabaseFactory
import model.user.UserRequest
import model.user.UserResponse
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import security.hashing.SaltedHash
import storage.CloudStorageService
import tables.UserTable
import util.getPreferGreetAt

class ProfileRepositoryImpl(
    private val db: DatabaseFactory,
    private val cloudStorageService: CloudStorageService
): ProfileRepository {
    override suspend fun greetUser(uid: String): String {
        val greeting = getPreferGreetAt()
        return db.dbQuery {
            UserTable.select {
                UserTable.uid eq uid
            }.map { it[UserTable.username] }.first()
        }.let { "$greeting, $it" }
    }

    override suspend fun getUser(uid: String): UserResponse {
        return db.dbQuery {
            UserTable.select {
                UserTable.uid eq uid
            }.map { it.toUserResponse() }.first()
        }
    }

    override suspend fun updateUser(uid: String, request: UserRequest, fileByte: ByteArray?): UserResponse {
        return db.dbQuery {

            UserTable.update({ UserTable.uid eq uid }) {
                it[username] = request.username
                it[name] = request.name
                it[phoneNumber] = request.phoneNumber
                it[email] = request.email
            }

            if (fileByte != null) {
                val imageUrl = cloudStorageService.run { fileByte.uploadFile("users/$uid/") }

                UserTable.update({ UserTable.uid eq uid }) {
                    it[avatar] = imageUrl
                }
            }

            UserTable.select {
                UserTable.uid eq uid
            }.map { it.toUserResponse() }.first()
        }
    }

    override suspend fun resetPassword(uid: String, saltedHash: SaltedHash) {
        db.dbQuery {
            UserTable.update({ UserTable.uid eq uid }) {
                it[password] = saltedHash.hash
                it[salt] = saltedHash.salt
            }
        }
    }

    override suspend fun isUsernameExist(uid: String, username: String): Boolean {
        return db.dbQuery {
            val currentUsername = UserTable.select { UserTable.uid eq uid }.map { it[UserTable.username] }.first()
            if (currentUsername == username) return@dbQuery false
            UserTable.select {
                UserTable.username eq username
            }.count() > 0
        }
    }

    override suspend fun isEmailExist(uid: String, email: String): Boolean {
        return db.dbQuery {
            val currentEmail = UserTable.select { UserTable.uid eq uid }.map { it[UserTable.email] }.first()
            if (currentEmail == email) return@dbQuery false
            UserTable.select {
                UserTable.email eq email
            }.count() > 0
        }
    }

    override suspend fun isPhoneNumberExist(uid: String, phoneNumber: String): Boolean {
        return db.dbQuery {
            val currentPhoneNumber = UserTable.select { UserTable.uid eq uid }.map { it[UserTable.phoneNumber] }.first()
            if (currentPhoneNumber == phoneNumber) return@dbQuery false
            UserTable.select {
                UserTable.phoneNumber eq phoneNumber
            }.count() > 0
        }
    }

    private fun ResultRow.toUserResponse() = UserResponse(
            email = this[UserTable.email],
            username = this[UserTable.username],
            avatar = this[UserTable.avatar],
            name = this[UserTable.name],
            phoneNumber = this[UserTable.phoneNumber]
        )

}