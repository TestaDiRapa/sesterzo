package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.UserApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.model.dto.PublicKeyPayload
import org.testadirapa.sesterzo.services.AuthService

class UserApiImpl(
	httpConfig: HttpConfig,
	private val authService: AuthService,
) : AbstractApi(httpConfig), UserApi {

	override val baseSegment: String = "user"

	override suspend fun getCurrentUser(): HttpResponse<User> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "current")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	override suspend fun setPublicKeyForCurrentUser(publicKey: Base64String): HttpResponse<User> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "current", "publicKey")
			}
			bearerAuth(authService.getJwt())
			accept(Application.Json)
			contentType(Application.Json)
			setBody(PublicKeyPayload(publicKey))
		}.wrap()

	override suspend fun setBackupConfirmation(): HttpResponse<User> =
		put {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "current", "hasBackup")
			}
			bearerAuth(authService.getJwt())
			accept(Application.Json)
		}.wrap()
}