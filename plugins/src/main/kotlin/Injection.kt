import di.databaseModule
import di.tokenModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureInjection() {
    install(Koin) {
        modules(databaseModule)
        modules(tokenModule)
    }
}