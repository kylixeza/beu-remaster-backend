package tables

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {

    override val tableName: String = "user"

    val uid = varchar("uid", 256)
    val username = varchar("username", 64)
    val name = varchar("name", 64)
    val avatar = varchar("avatar", 512)
    val phoneNumber = varchar("phone_number", 24)
    val email = varchar("email", 64)

    val password = varchar("password", 1024)
    val salt = varchar("salt", 1024)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(uid)


}