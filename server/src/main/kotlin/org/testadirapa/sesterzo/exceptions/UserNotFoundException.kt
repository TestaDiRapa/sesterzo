package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class UserNotFoundException(userId: String) : HttpException("User $userId not found")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.NotFound
	override val label: ExceptionLabel = ExceptionLabel.UserNotFound
}
