import base.buildErrorJson
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import security.token.TokenService
import java.util.Date

class Middleware(
    private val tokenService: TokenService
) {
    private suspend fun ApplicationCall.validateToken() {
        val jwt = request.header("Authorization")?.substring("Bearer ".length)
        val isValid = tokenService.run { isTokenValid(jwt) }

        jwt?.let {
            val decodedJWT = tokenService.run { decode(jwt) }
            val expirationDate = decodedJWT.expiresAt
            val currentDate = Date()
            if (expirationDate.before(currentDate)) {
                buildErrorJson(httpStatusCode = Unauthorized, message = "Token expired")
            }
            return
        }

        if (!isValid) {
            buildErrorJson(httpStatusCode = Unauthorized, message = "Invalid token")
        }
        return
    }

    private inline fun<reified T: Any> ApplicationCall.getClaim(claimName: String) = kotlin.run {
        val principal = principal<JWTPrincipal>()
        principal?.getClaim(claimName, T::class)
    }

    private fun ApplicationCall.getUidClaim() = getClaim<String>("uid")

    suspend fun Route.authenticate(call: ApplicationCall, http: HTTPVerb, route: String, onAction: (String) -> Unit) = authenticate {

        suspend fun internalAuthentication() {
            call.validateToken()
            val uid = call.getUidClaim()
            onAction(uid.orEmpty())
        }

        when(http) {
            HTTPVerb.GET -> get(route) { internalAuthentication() }
            HTTPVerb.POST -> post(route) { internalAuthentication() }
            HTTPVerb.PUT -> put(route) { internalAuthentication() }
            HTTPVerb.DELETE -> delete(route) { internalAuthentication() }
        }
    }
}