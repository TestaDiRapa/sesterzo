package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class ForbiddenException(msg: String) : HttpException(msg)  {
	override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
	override val label: ExceptionLabel = ExceptionLabel.GenericForbidden
}
