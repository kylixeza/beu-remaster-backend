import base.buildErrorResponse
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import security.token.Config.tokenConfig

fun Application.configureSecurity() {
    authentication {
        jwt {
            val tokenConfig = tokenConfig
            realm = System.getenv("JWT_REALM")
            verifier(
                JWT
                    .require(Algorithm.HMAC256(tokenConfig.secret))
                    .withAudience(tokenConfig.audience)
                    .withIssuer(tokenConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(tokenConfig.audience)) JWTPrincipal(credential.payload) else null
            }
            challenge {_, _ ->
                call.buildErrorResponse(httpStatusCode = HttpStatusCode.Unauthorized, message = "Token expired or invalid")
            }
        }
    }
}
