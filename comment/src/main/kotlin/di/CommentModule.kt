package di

import controller.CommentController
import controller.CommentControllerImpl
import org.koin.dsl.module
import repository.CommentRepository
import repository.CommentRepositoryImpl
import route.CommentRoute

val commentModule = module {
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<CommentController> { CommentControllerImpl(get()) }
    single { CommentRoute(get(), get()) }
}