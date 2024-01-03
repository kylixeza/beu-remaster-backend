import database.DatabaseFactory
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.selectAll
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import tables.UserTable

fun Application.configureRouting() {
    val database by inject<DatabaseFactory>()
    install(Resources)
    routing {
        get("/") {
            database.dbQuery { UserTable.selectAll().toList() }
        }
        get<Articles> { article ->
            // Get all articles ...
            call.respond("List of articles sorted starting from ${article.sort}")
        }
    }
}

@Serializable
@Resource("/articles")
class Articles(val sort: String? = "new")
