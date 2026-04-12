package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class InvalidSpaceAuthorizationException(
	spaceId: String
) : HttpException("You are not authorized to perform this operation on space $spaceId") {
	override val statusCode: HttpStatusCode = HttpStatusCode.Forbidden
	override val label: ExceptionLabel = ExceptionLabel.ForbiddenInSpace
}
