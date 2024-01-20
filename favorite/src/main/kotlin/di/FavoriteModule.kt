package di

import controller.FavoriteController
import controller.FavoriteControllerImpl
import org.koin.dsl.module
import repository.FavoriteRepository
import repository.FavoriteRepositoryImpl
import route.FavoriteRoute

val favoriteModule = module {
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    single<FavoriteController> { FavoriteControllerImpl(get()) }
    single { FavoriteRoute(get(), get()) }
}