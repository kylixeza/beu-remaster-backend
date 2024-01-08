package security.token

object Config {

    val tokenConfig: TokenConfig
        get() = TokenConfig(
            issuer = System.getenv("JWT_ISSUER"),
            audience = System.getenv("JWT_AUDIENCE"),
            expiresIn = 1000L * 60L * 60L * 24L * 31L,
            secret = System.getenv("JWT_SECRET")
        )

}