package repository

import database.DatabaseFactory
import model.help_center.HelpCenterRequest
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import tables.HelpCenterTable
import tables.UserTable
import util.TicketStatus

class HelpCenterRepositoryImpl(
    private val db: DatabaseFactory
): HelpCenterRepository {

    override suspend fun createTicket(uid: String, body: HelpCenterRequest): String {
        return db.dbQuery {
            val latestTicketIdNumber = HelpCenterTable.selectAll()
                .orderBy(HelpCenterTable.ticketId to SortOrder.DESC)
                .limit(1)
                .singleOrNull()?.get(HelpCenterTable.ticketId)?.removePrefix("#TICKET-")?.toInt() ?: 0

            val ticketId = "#TICKET-${latestTicketIdNumber + 1}"

            HelpCenterTable.insert {
                it[this.uid] = uid
                it[this.message] = body.message
                it[this.status] = TicketStatus.OPEN
                it[this.ticketId] = ticketId
            }

            ticketId
        }
    }

    override suspend fun getName(uid: String): String {
        return db.dbQuery {
            UserTable.select { UserTable.uid eq uid }
                .singleOrNull()?.get(UserTable.name).orEmpty()
        }
    }

    override suspend fun getEmail(uid: String): String {
        return db.dbQuery {
            UserTable.select { UserTable.uid eq uid }
                .singleOrNull()?.get(UserTable.email).orEmpty()
        }
    }
}