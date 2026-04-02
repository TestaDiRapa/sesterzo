package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class InvalidRegistrationParametersException() : HttpException("Invalid email or name") {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.InvalidRegistrationParameters
}
