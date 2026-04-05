package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class EntityNotFoundException(
	entityId: String,
	override val label: ExceptionLabel
) : HttpException("Entity $entityId not found")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.NotFound
}
