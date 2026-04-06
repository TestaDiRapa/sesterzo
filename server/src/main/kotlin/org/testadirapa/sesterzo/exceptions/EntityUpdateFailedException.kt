package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class EntityUpdateFailedException(
	entityId: String,
	override val label: ExceptionLabel
) : HttpException("Update of entity $entityId not found")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
}
