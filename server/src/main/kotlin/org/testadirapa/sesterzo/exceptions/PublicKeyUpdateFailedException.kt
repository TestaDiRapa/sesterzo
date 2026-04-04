package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class PublicKeyUpdateFailedException(userId: String) : HttpException("Cannot set public key for user $userId")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.PublicKeyUpdateFailed
}
