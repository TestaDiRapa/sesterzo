package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class InvalidCaptchaException : HttpException("Invalid captcha") {
	override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
	override val label: ExceptionLabel = ExceptionLabel.InvalidCaptcha
}
