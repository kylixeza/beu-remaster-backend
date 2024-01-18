package di

import controller.HistoryController
import controller.HistoryControllerImp
import org.koin.dsl.module
import repository.HistoryRepository
import repository.HistoryRepositoryImpl
import route.HistoryRoute

val historyModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<HistoryController> { HistoryControllerImp(get()) }
    single { HistoryRoute(get(), get()) }
}