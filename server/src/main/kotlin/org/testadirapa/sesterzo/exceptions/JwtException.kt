package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

open class JwtException(msg: String) : HttpException(msg)  {
	override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
	override val label: ExceptionLabel = ExceptionLabel.InvalidJWT
}
