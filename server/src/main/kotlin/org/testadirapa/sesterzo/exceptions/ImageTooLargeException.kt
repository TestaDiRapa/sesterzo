package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class ImageTooLargeException : HttpException("You cannot upload images over 10Mb") {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.ImageTooLarge
}
