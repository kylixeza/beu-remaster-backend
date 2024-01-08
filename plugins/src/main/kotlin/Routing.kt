import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import route.AuthRoute

fun Application.configureRouting() {
    val authRoute by inject<AuthRoute>()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoute.apply { initRoutes() }
    }
}
