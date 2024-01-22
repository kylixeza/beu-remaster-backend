package di

import controller.ProfileController
import controller.ProfileControllerImpl
import org.koin.dsl.module
import repository.ProfileRepository
import repository.ProfileRepositoryImpl
import route.ProfileRoute

val profileModule = module {
    single<ProfileController> { ProfileControllerImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single { ProfileRoute(get(), get()) }
}