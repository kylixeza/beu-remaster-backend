package security.token

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.*
import security.token.TokenClaim
import security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    suspend fun Application.invalidate(token: String, saveToDb: suspend String.() -> Unit)

    suspend fun insertToBlacklist(token: String)

    suspend fun isTokenValid(token: String?): Boolean
}