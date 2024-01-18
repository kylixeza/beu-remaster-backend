import di.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureInjection() {
    install(Koin) {
        modules(
            databaseModule,
            tokenModule,
            middlewareModule,
            authModule,
            recipeModule,
            categoryModule,
            nutritionModule,
            commentModule,
            historyModule,
        )
    }
}