package di

import controller.ReviewController
import controller.ReviewControllerImpl
import org.koin.dsl.module
import repository.ReviewRepository
import repository.ReviewRepositoryImpl
import route.ReviewRoute

val reviewModule = module {
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
    single<ReviewController> { ReviewControllerImpl(get()) }
    single { ReviewRoute(get(), get()) }
}