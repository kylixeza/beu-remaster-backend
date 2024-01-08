package security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import database.DatabaseFactory
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import security.token.Config.tokenConfig
import tables.TokenBlacklistTable

class JWTTokenService(
    private val dbFactory: DatabaseFactory
): TokenService {
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

    override suspend fun insertToBlacklist(token: String) {
        dbFactory.dbQuery {
            TokenBlacklistTable.insert {
                it[TokenBlacklistTable.token] = token
            }
        }
    }

    override suspend fun isTokenValid(token: String?): Boolean = dbFactory.dbQuery {
        if (token == null) return@dbQuery false
        TokenBlacklistTable.select {
            TokenBlacklistTable.token eq token
        }.empty()
    }
}