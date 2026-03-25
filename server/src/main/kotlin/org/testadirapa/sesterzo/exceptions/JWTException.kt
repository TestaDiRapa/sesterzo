package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

open class JWTException(msg: String) : HttpException(msg)  {
	override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
	override val label: ExceptionLabel = ExceptionLabel.InvalidJWT
}
