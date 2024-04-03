package controller

import base.buildErrorResponse
import base.buildSuccessResponse
import email.EmailService
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.help_center.HelpCenterRequest
import repository.HelpCenterRepository

class HelpCenterControllerImpl(
    private val emailService: EmailService,
    private val helpCenterRepository: HelpCenterRepository
): HelpCenterController {

    override suspend fun ApplicationCall.sendEmail(uid: String) {

        val body = receive<HelpCenterRequest>()
        val ticketSubject = helpCenterRepository.createTicket(uid, body)

        val name = helpCenterRepository.getName(uid)
        val email = helpCenterRepository.getEmail(uid)

        emailService.sendEmail(
            message = body.message,
            name = name,
            to = email,
            ticketSubject = ticketSubject,
            onSuccess = { buildSuccessResponse { "Email berhasil dikirimkan" } },
            onError = { buildErrorResponse(it)}
        )
    }

}