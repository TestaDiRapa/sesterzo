package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class InvalidRegistrationException(
	processId: String,
	token: String
) : HttpException("Invalid registration $processId: $token") {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.InvalidRegistration
}
