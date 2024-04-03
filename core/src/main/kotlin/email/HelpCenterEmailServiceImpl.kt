package email

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class HelpCenterEmailServiceImpl(
    private val properties: Properties
): EmailService {

    override suspend fun sendEmail(
        message: String,
        name: String,
        to: String,
        ticketSubject: String,
        onSuccess: suspend () -> Unit,
        onError: suspend (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        val email = System.getenv("EMAIL_ADDRESS")
        val password = System.getenv("EMAIL_PASSWORD")
        val personal = System.getenv("EMAIL_PERSONAL")

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        })

        try {
            val currentDir = System.getProperty("user.dir")
            val resourcesDir = "$currentDir/core/src/main/resources"

            val templatePath = "$resourcesDir/beu_email_template.html"
            var emailContent = String(Files.readAllBytes(Paths.get(templatePath)))
            val subject = "Pusat Bantuan Beu: $ticketSubject"

            emailContent = emailContent.replace("\${name}", name)
                .replace("\${helpMessage}", message)

            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(email, personal))
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(to))
            mimeMessage.subject = subject
            mimeMessage.setContent(emailContent, "text/html")

            Transport.send(mimeMessage)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }

    }

}