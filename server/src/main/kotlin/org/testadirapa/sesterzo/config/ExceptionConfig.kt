package org.testadirapa.sesterzo.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.error
import org.testadirapa.sesterzo.exceptions.UnauthorizedException
import org.testadirapa.sesterzo.models.StatusResponse
import java.io.IOException

private val logger = KtorSimpleLogger("com.example.RequestTracePlugin")

fun Application.configureExceptions() {
	fun Exception.toErrorResponse(status: HttpStatusCode) =
		StatusResponse(
			false,
			message ?: this::class.qualifiedName ?: "Something wrong occurred",
			status.value,
		)

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			when (cause) {
				is AccessDeniedException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IllegalAccessException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IOException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized, cause.toErrorResponse(HttpStatusCode.Unauthorized))
				is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.toErrorResponse(HttpStatusCode.NotFound))
				else -> {
					logger.error(cause)
					call.respond(
						HttpStatusCode.InternalServerError,
						StatusResponse(false, cause.message ?: "Something went wrong", HttpStatusCode.InternalServerError.value),
					)
				}
			}
		}
	}
}
