package route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.FileNotFoundException

class StaticResourcesRoute {

    fun Route.privacyPolicy() {
        get("/privacy-policy") {
            val htmlContent = call.application.getResourceAsText("files/privacy_policy.html")
            call.respondText(htmlContent, ContentType.Text.Html)
        }
    }

    fun Route.termsAndConditions() {
        get("/terms-and-conditions") {
            val htmlContent = call.application.getResourceAsText("files/terms_and_conditions.html")
            call.respondText(htmlContent, ContentType.Text.Html)
        }
    }

    private fun Application.getResourceAsText(path: String): String {
        return this::class.java.classLoader.getResourceAsStream(path)?.bufferedReader()?.use { it.readText() }
            ?: throw FileNotFoundException("Resource not found: $path")
    }

}