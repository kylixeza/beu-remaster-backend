package controller

import io.ktor.server.application.*

interface FavoriteController {
    suspend fun ApplicationCall.insertFavorite(uid: String, recipeId: String)
    suspend fun ApplicationCall.deleteFavorite(uid: String, recipeId: String)
    suspend fun ApplicationCall.getFavorites(uid: String)
}