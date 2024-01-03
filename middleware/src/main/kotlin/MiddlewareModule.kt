import org.koin.dsl.module

val middlewareModule = module {
    single { Middleware(get()) }
}