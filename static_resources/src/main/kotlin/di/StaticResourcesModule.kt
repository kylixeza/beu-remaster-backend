package di

import route.StaticResourcesRoute
import org.koin.dsl.module

val staticResourcesModule = module {
    single { StaticResourcesRoute() }
}