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
import util.getPreferGreetAt

class ProfileControllerImpl(
    private val repository: ProfileRepository,
    private val hashService: HashingService
): ProfileController {
    override suspend fun ApplicationCall.greetUser(uid: String) {
        val greeting = getPreferGreetAt()
        buildSuccessResponse {
            val username = repository.greetUser(uid)
            "$greeting, $username"
        }
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
                        val currentUser = repository.getUser(uid)
                        val isUsernameExist = repository.isUsernameExist(uid, username = body?.username.orEmpty())
                        val isEmailExist = repository.isEmailExist(uid, email = body?.email.orEmpty())

                        if (isUsernameExist && currentUser.username != body?.username) {
                            buildErrorResponse(HttpStatusCode.BadRequest, message = "Username already in use")
                            return@forEachPart
                        }

                        if (isEmailExist && currentUser.email != body?.email) {
                            buildErrorResponse(HttpStatusCode.BadRequest, message = "Email already in use")
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
        buildSuccessResponse("Password berhasil diubah") { repository.resetPassword(uid, saltedHash) }
    }
}