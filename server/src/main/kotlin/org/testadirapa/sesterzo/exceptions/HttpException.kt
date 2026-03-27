package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

abstract class HttpException(
	msg: String,
): Exception(msg) {

	abstract val label: ExceptionLabel
	abstract val statusCode: HttpStatusCode

	fun toStatusResponse() = StatusResponse(
		message = message,
		code = statusCode.value,
		label = label
	)
}