import di.databaseModule
import io.ktor.server.application.*
import io.ktor.server.application.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ktor.plugin.Koin

fun Application.configureInjection() {
    install(Koin) {
        modules(databaseModule)
    }
}