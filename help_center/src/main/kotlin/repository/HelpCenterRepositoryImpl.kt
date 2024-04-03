package repository

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
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
            val latestTicketSubjectNumber = HelpCenterTable.selectAll()
                .orderBy(HelpCenterTable.ticketSubject to SortOrder.DESC)
                .limit(1)
                .singleOrNull()?.get(HelpCenterTable.ticketSubject)?.removePrefix("#TICKET-")?.toInt() ?: 0

            val ticketSubject = "#TICKET-${latestTicketSubjectNumber + 1}"

            HelpCenterTable.insert {
                it[this.ticketId] = "TICKET-${NanoIdUtils.randomNanoId()}"
                it[this.ticketSubject] = ticketSubject
                it[this.uid] = uid
                it[this.message] = body.message
                it[this.status] = TicketStatus.OPEN
            }

            ticketSubject
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