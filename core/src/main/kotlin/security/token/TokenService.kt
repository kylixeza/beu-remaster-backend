package security.token

import io.ktor.server.application.*
import security.token.TokenClaim
import security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    suspend fun Application.invalidate(token: String, saveToDb: suspend String.() -> Unit)
}