package controller

import io.ktor.server.application.*

interface HelpCenterController {

    suspend fun ApplicationCall.sendEmail(uid: String)


}