package base

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

suspend inline fun <reified T> ApplicationCall.buildSuccessResponse(
    messagePlaceholder: String? = null,
    noinline action: suspend () -> T
) {
    try {
        val data = action()
        this.respond(
            HttpStatusCode.OK,
            BaseResponse(
                HttpStatusCode.OK.value,
                "Success",
                if (data is Unit) messagePlaceholder else data
            )
        )
    } catch (e: Exception) {
        this@buildSuccessResponse.buildErrorResponse(e)
    }
}

suspend inline fun ApplicationCall.buildErrorResponse(exception: Exception) {
    when (exception) {
        is BadRequestException -> this.respond(
            HttpStatusCode.BadRequest,
            BaseResponse(HttpStatusCode.BadRequest.value, exception.message.toString(),null)
        )

        is NotFoundException -> this.respond(
            HttpStatusCode.NotFound,
            BaseResponse(HttpStatusCode.NotFound.value, exception.message.toString(),null)
        )

        else -> this.respond(
            HttpStatusCode.Conflict,
            BaseResponse(HttpStatusCode.Conflict.value, exception.message.toString(),null)
        )
    }
}

suspend inline fun ApplicationCall.buildErrorResponse(httpStatusCode: HttpStatusCode = HttpStatusCode.Conflict, message: String) {
    this.respond(httpStatusCode, BaseResponse(httpStatusCode.value, message,null))
}

suspend inline fun <reified T> ApplicationCall.buildSuccessListResponse(noinline action: suspend () -> T) {
    try {
        val data = action()
        val count = count { data as List<*> }
        this.respond(
            HttpStatusCode.OK,
            BaseListResponse(
                HttpStatusCode.OK.value,
                "Request Success",
                count,
                data
            )
        )

    } catch (e: Exception) {
        this@buildSuccessListResponse.buildErrorListResponse(e)
    }
}

suspend inline fun ApplicationCall.buildErrorListResponse(e: Exception) {
    val listResponse = BaseListResponse(message = e.message.toString(), count = 0, data = arrayListOf<Any>())
    when (e) {
        is BadRequestException -> {
            listResponse.statusCode = HttpStatusCode.BadRequest.value
            this.respond(
                HttpStatusCode.BadRequest,
                listResponse
            )
        }

        is NotFoundException -> {
            listResponse.statusCode = HttpStatusCode.NotFound.value
            this.respond(
                HttpStatusCode.NotFound,
                listResponse
            )
        }

        else -> {
            listResponse.statusCode = HttpStatusCode.Conflict.value
            this.respond(
                HttpStatusCode.NotFound,
                listResponse
            )
        }
    }
}

inline fun count(block: () -> List<*>) = block().size
