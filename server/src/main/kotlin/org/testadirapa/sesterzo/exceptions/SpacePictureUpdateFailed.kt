package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class SpacePictureUpdateFailed(spaceId: String) : HttpException("Cannot update space thumbnail for $spaceId")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.SpaceThumbnailUpdateFailed
}