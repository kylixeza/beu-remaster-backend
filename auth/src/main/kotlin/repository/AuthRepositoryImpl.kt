package repository

import database.DatabaseFactory
import model.User
import model.auth.RegisterRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import security.hashing.SaltedHash
import tables.UserTable
import java.util.*

class AuthRepositoryImpl(
    private val dbFactory: DatabaseFactory,
): AuthRepository {
    override suspend fun isEmailExist(email: String): Boolean = dbFactory.dbQuery {
        UserTable.select {
            UserTable.email eq email
        }.empty().not()
    }

    override suspend fun isPhoneNumberExist(phoneNumber: String): Boolean = dbFactory.dbQuery {
        UserTable.select {
            UserTable.phoneNumber eq phoneNumber
        }.empty().not()
    }

    override suspend fun isUsernameExist(username: String): Boolean = dbFactory.dbQuery {
        UserTable.select {
            UserTable.username eq username
        }.empty().not()
    }

    override suspend fun insertUser(body: RegisterRequest, saltedHash: SaltedHash): String = dbFactory.dbQuery {
        val uid = UUID.randomUUID().toString()
        UserTable.insert {
            it[UserTable.uid] = uid
            it[username] = body.username
            it[password] = saltedHash.hash
            it[salt] = saltedHash.salt
            it[avatar] = ""
            it[name] = body.name
            it[phoneNumber] = body.phoneNumber
        } get UserTable.uid
    }

    override suspend fun getUserByIdentifier(identifier: String): User? = dbFactory.dbQuery {
        UserTable.select {
            (UserTable.email eq identifier) or (UserTable.phoneNumber eq identifier) or (UserTable.username eq identifier)
        }.map {
            User(
                uid = it[UserTable.uid],
                email = it[UserTable.email],
                name = it[UserTable.name],
                password = it[UserTable.password],
                salt = it[UserTable.salt],
            )
        }.singleOrNull()
    }

}