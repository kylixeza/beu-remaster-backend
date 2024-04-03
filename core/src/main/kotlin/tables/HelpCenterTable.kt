package tables

import org.jetbrains.exposed.sql.Table
import util.TicketStatus

object HelpCenterTable: Table() {

    override val tableName: String = "help_center"

    val ticketId = varchar("ticket_id", 255)
    val ticketSubject = varchar("ticket_subject", 24)
    val uid = reference("uid", UserTable.uid)
    val message = text("message")
    val status = enumeration("status", TicketStatus::class)

    override val primaryKey: PrimaryKey = PrimaryKey(ticketId)
}