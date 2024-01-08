package di

import controller.AuthController
import controller.AuthControllerImpl
import org.koin.dsl.module
import repository.AuthRepository
import repository.AuthRepositoryImpl
import route.AuthRoute

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<AuthController> { AuthControllerImpl(get(), get(), get()) }
    factory<AuthRoute> { AuthRoute(get()) }
}