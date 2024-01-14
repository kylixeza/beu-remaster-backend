import base.buildErrorResponse
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

        if (!isValid) {
            buildErrorResponse(httpStatusCode = Unauthorized, message = "Invalid token")
        }
        return
    }

    private inline fun<reified T: Any> ApplicationCall.getClaim(claimName: String) = kotlin.run {
        val principal = principal<JWTPrincipal>()
        principal?.getClaim(claimName, T::class)
    }

    fun ApplicationCall.getUidClaim() = getClaim<String>("uid")

    fun Route.authenticate(http: HTTPVerb, routes: String = "", onAction: suspend (String, ApplicationCall) -> Unit) = authenticate {

        suspend fun ApplicationCall.internalAuthentication() {
            validateToken()
            val uid = getUidClaim()
            onAction(uid.orEmpty(), this)
        }

        when(http) {
            HTTPVerb.GET -> get(routes) { call.internalAuthentication() }
            HTTPVerb.POST -> post(routes) { call.internalAuthentication() }
            HTTPVerb.PUT -> put(routes) { call.internalAuthentication() }
            HTTPVerb.DELETE -> delete(routes) { call.internalAuthentication() }
        }
    }
}