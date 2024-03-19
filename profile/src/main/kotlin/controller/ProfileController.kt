package controller

import io.ktor.server.application.*

interface ProfileController {
    suspend fun ApplicationCall.greetUser(uid: String)
    suspend fun ApplicationCall.getUser(uid: String)
    suspend fun ApplicationCall.updateUser(uid: String)
    suspend fun ApplicationCall.resetPassword(uid: String)
}