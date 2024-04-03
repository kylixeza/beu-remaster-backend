package email

interface EmailService {

    suspend fun sendEmail(
        message: String,
        name: String,
        to: String,
        ticketSubject: String,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    )

}