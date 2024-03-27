package controller

import base.buildErrorResponse
import base.buildSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.auth.LoginRequest
import model.auth.RegisterRequest
import model.auth.TokenResponse
import repository.AuthRepository
import security.hashing.HashingService
import security.hashing.SaltedHash
import security.token.Config.tokenConfig
import security.token.TokenClaim
import security.token.TokenService

class AuthControllerImpl(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService,
    private val hashService: HashingService
): AuthController {
    override suspend fun ApplicationCall.register() {
        val body = receive<RegisterRequest>()
        val isPhoneNumberExist = authRepository.isPhoneNumberExist(body.phoneNumber)
        val isUsernameExist = authRepository.isUsernameExist(body.username)

        if (isPhoneNumberExist) {
            buildErrorResponse(message = "Nomor telepon sudah digunakan")
            return
        }

        if (isUsernameExist) {
            buildErrorResponse(message = "Username sudah digunakan")
            return
        }

        val saltedHash = hashService.generateSaltedHash(body.password)
        val uid = authRepository.insertUser(body, saltedHash)
        val token = tokenService.generate(tokenConfig, TokenClaim("uid", uid))
        buildSuccessResponse { TokenResponse(token) }
    }

    override suspend fun ApplicationCall.login() {
        val body = receive<LoginRequest>()

        val isEmailExist = authRepository.isEmailExist(body.identifier)
        val isPhoneNumberExist = authRepository.isPhoneNumberExist(body.identifier)
        val isUsernameExist = authRepository.isUsernameExist(body.identifier)

        if (!isEmailExist && !isPhoneNumberExist && !isUsernameExist) {
            buildErrorResponse(message = "Pengguna tidak ditemukan, silahkan daftar terlebih dahulu")
            return
        }

        val user = authRepository.getUserByIdentifier(body.identifier)
        if (user == null) {
            buildErrorResponse(message = "Pengguna tidak ditemukan, silahkan daftar terlebih dahulu")
            return
        }

        val isPasswordValid = hashService.verify(body.password, SaltedHash(user.password, user.salt))
        if (!isPasswordValid) {
            buildErrorResponse(message = "Password salah, silahkan coba lagi")
            return
        }

        val token = tokenService.generate(tokenConfig, TokenClaim("uid", user.uid))
        buildSuccessResponse { TokenResponse(token) }
    }

    override suspend fun ApplicationCall.logout() {
        val token = request.header("Authorization")?.substring("Bearer ".length).orEmpty()
        tokenService.insertToBlacklist(token)
        buildSuccessResponse { "Berhasil keluar" }
    }
}