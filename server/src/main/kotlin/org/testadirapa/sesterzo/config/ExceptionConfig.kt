package org.testadirapa.sesterzo.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.error
import org.testadirapa.sesterzo.exceptions.HttpException
import org.testadirapa.sesterzo.model.dto.StatusResponse
import java.io.IOException

private val logger = KtorSimpleLogger("com.example.RequestTracePlugin")

fun Application.configureExceptions() {
	fun Exception.toErrorResponse(status: HttpStatusCode) =
		StatusResponse(
			message = message ?: this::class.qualifiedName ?: "Something wrong occurred",
			code = status.value,
			label = null
		)

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			when (cause) {
				is HttpException -> call.respond(cause.statusCode, cause.toStatusResponse())
				is AccessDeniedException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IllegalAccessException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IOException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				else -> {
					logger.error(cause)
					call.respond(
						HttpStatusCode.InternalServerError,
						StatusResponse(
							message = cause.message ?: "Something went wrong",
							code = HttpStatusCode.InternalServerError.value,
							label = null
						),
					)
				}
			}
		}
	}
}
