import base.buildErrorJson
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import security.token.TokenService
import java.util.Date

class Middleware(
    private val tokenService: TokenService
) {
    suspend fun ApplicationCall.validateToken() {
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

    inline fun<reified T: Any> getClaim(call: ApplicationCall, claimName: String) = kotlin.run {
        val principal = call.principal<JWTPrincipal>()
        principal?.getClaim(claimName, T::class)
    }
}