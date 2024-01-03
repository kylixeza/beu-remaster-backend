package security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import security.token.Config.tokenConfig

class JWTTokenService: TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token =  JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach {
            token = token.withClaim(it.name, it.value)
        }

        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override suspend fun Application.invalidate(token: String, saveToDb: suspend String.() -> Unit) {
        try {
            JWT.require(Algorithm.HMAC256(tokenConfig.secret))
                .withAudience(tokenConfig.audience)
                .withIssuer(tokenConfig.issuer)
                .build()
                .verify(token)
            token.saveToDb()
        } catch (e: Exception) {
            throw IllegalAccessException(e.toString())
        }
    }
}