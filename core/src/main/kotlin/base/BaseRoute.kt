package base

import io.ktor.server.routing.*

interface BaseRoute {
    fun Route.routes()
}