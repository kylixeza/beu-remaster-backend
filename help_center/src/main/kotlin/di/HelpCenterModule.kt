package di

import controller.HelpCenterController
import controller.HelpCenterControllerImpl
import org.koin.dsl.module
import repository.HelpCenterRepository
import repository.HelpCenterRepositoryImpl
import route.HelpCenterRoute

val helpCenterModule = module {
    single<HelpCenterRepository> { HelpCenterRepositoryImpl(get()) }
    single<HelpCenterController> { HelpCenterControllerImpl(get(), get()) }
    single { HelpCenterRoute(get(), get()) }
}