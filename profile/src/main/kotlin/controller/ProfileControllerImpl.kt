package controller

import base.buildErrorResponse
import base.buildSuccessResponse
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import model.user.UserRequest
import repository.ProfileRepository
import com.google.gson.Gson
import io.ktor.http.*
import model.user.PasswordRequest
import security.hashing.HashingService

class ProfileControllerImpl(
    private val repository: ProfileRepository,
    private val hashService: HashingService
): ProfileController {
    override suspend fun ApplicationCall.greetUser(uid: String) {
        buildSuccessResponse { repository.greetUser(uid) }
    }

    override suspend fun ApplicationCall.getUser(uid: String) {
        buildSuccessResponse { repository.getUser(uid)  }
    }

    override suspend fun ApplicationCall.updateUser(uid: String) {

        val multipart = receiveMultipart()
        var body: UserRequest? = null
        var fileByte: ByteArray? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "body") {
                        val isUsernameExist = repository.isUsernameExist(uid, username = body?.username.orEmpty())
                        val isEmailExist = repository.isEmailExist(uid, email = body?.email.orEmpty())
                        val isPhoneNumberExist = repository.isPhoneNumberExist(uid, phoneNumber = body?.phoneNumber.orEmpty())

                        if (isUsernameExist) {
                            buildErrorResponse(HttpStatusCode.BadRequest, message = "Username sudah digunakan")
                            return@forEachPart
                        }

                        if (isEmailExist) {
                            buildErrorResponse(HttpStatusCode.BadRequest, message = "Email sudah digunakan")
                            return@forEachPart
                        }

                        if (isPhoneNumberExist) {
                            buildErrorResponse(HttpStatusCode.BadRequest, message = "Nomor telepon sudah digunakan")
                            return@forEachPart
                        }

                        Gson().fromJson(part.value, UserRequest::class.java).let { body = it }
                    }
                }
                is PartData.FileItem -> {
                    if (part.name == "image") {
                        fileByte = part.streamProvider().readBytes()
                    }
                }
                else -> {}
            }
            part.dispose()
        }

        if (body != null) {
            buildSuccessResponse { repository.updateUser(uid, body!!, fileByte) }
        } else {
            buildErrorResponse(message = "Profil gagal diperbarui")
        }
    }

    override suspend fun ApplicationCall.resetPassword(uid: String) {

        val body = try {
            receive<PasswordRequest>()
        } catch (e: Exception) {
            buildErrorResponse(e)
            return
        }

        val saltedHash = hashService.generateSaltedHash(body.password)
        buildSuccessResponse { repository.resetPassword(uid, saltedHash) }
    }
}