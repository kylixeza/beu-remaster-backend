package repository

import model.help_center.HelpCenterRequest

interface HelpCenterRepository {

    suspend fun createTicket(uid: String, body: HelpCenterRequest): String
    suspend fun getName(uid: String): String
    suspend fun getEmail(uid: String): String
}