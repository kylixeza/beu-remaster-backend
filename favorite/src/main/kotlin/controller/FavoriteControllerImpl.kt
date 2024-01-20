package controller

import base.buildSuccessListResponse
import base.buildSuccessResponse
import io.ktor.server.application.*
import repository.FavoriteRepository

class FavoriteControllerImpl(
    private val repository: FavoriteRepository
): FavoriteController {
    override suspend fun ApplicationCall.insertFavorite(uid: String, recipeId: String) {
        buildSuccessResponse { repository.insertFavorite(uid, recipeId)  }
    }

    override suspend fun ApplicationCall.deleteFavorite(uid: String, recipeId: String) {
        buildSuccessResponse { repository.deleteFavorite(uid, recipeId) }
    }

    override suspend fun ApplicationCall.getFavorites(uid: String) {
        buildSuccessListResponse { repository.getFavorites(uid) }
    }
}