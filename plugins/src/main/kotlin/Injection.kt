import di.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureInjection() {
    install(Koin) {
        modules(
            databaseModule,
            tokenModule,
            storageModule,
            emailModule,
            middlewareModule,
            authModule,
            recipeModule,
            categoryModule,
            nutritionModule,
            commentModule,
            historyModule,
            reviewModule,
            favoriteModule,
            profileModule,
            helpCenterModule,
            staticResourcesModule
        )
    }
}