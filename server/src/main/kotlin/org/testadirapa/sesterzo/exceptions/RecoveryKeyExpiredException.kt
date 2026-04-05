package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class RecoveryKeyExpiredException(recoveryKeyId: String) : HttpException("Recovery key $recoveryKeyId expired") {
	override val statusCode: HttpStatusCode = HttpStatusCode.Gone
	override val label: ExceptionLabel = ExceptionLabel.InvalidCaptcha
}
