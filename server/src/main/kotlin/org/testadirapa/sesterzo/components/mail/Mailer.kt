package org.testadirapa.sesterzo.components.mail

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import java.net.URLEncoder

class Mailer(
	private val config: MailerConfig,
) {
	companion object {
		@Serializable
		private data class MailInput(
			val id: String,
			val email: String,
			val attributes: Map<String, String>,
		)
	}

	private val httpClient =
		HttpClient(OkHttp) {
			install(ContentNegotiation) {
				json()
			}
		}

	suspend fun sendPasswordResetEmail(
		email: String,
		processId: String,
	) {
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = config.resetPasswordTemplateId,
					email = email,
					attributes =
						mapOf(
							"url" to config.homunculusUrl,
							"email" to URLEncoder.encode(email, "UTF-8"),
							"processId" to URLEncoder.encode(processId, "UTF-8"),
						),
				),
			)
		}
	}

	suspend fun sendInvitationEmail(
		email: String,
		tmpToken: String,
		inviterName: String,
	) {
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = config.inviteTemplateId,
					email = email,
					attributes =
						mapOf(
							"url" to config.homunculusUrl,
							"inviterName" to inviterName,
							"email" to URLEncoder.encode(email, "UTF-8"),
							"tmpToken" to URLEncoder.encode(tmpToken, "UTF-8"),
						),
				),
			)
		}
	}

}
