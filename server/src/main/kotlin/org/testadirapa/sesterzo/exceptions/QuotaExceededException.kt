package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class QuotaExceededException : HttpException("You can create no more than 5 spaces") {
	override val statusCode: HttpStatusCode = HttpStatusCode.PaymentRequired
	override val label: ExceptionLabel = ExceptionLabel.QuotaExceeded
}
