package org.testadirapa.sesterzo.components.mail.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.components.mail.Mailer
import org.testadirapa.sesterzo.components.mail.MailerConfig
import java.net.URLEncoder

class HermesMailer(
	private val config: MailerConfig.HermesMailerConfig,
): Mailer {
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

	private suspend fun sendTokenEmail(
		templateId: String,
		email: String,
		name: String,
		token: String
	) {
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = templateId,
					email = email,
					attributes =
						mapOf(
							"email" to URLEncoder.encode(email, "UTF-8"),
							"name" to URLEncoder.encode(name, "UTF-8"),
							"token" to URLEncoder.encode(token, "UTF-8"),
						),
				),
			)
		}
	}

	override suspend fun sendAccessTokenEmail(email: String, name: String, token: String) {
		sendTokenEmail(
			templateId = config.tokenTemplateId,
			email = email,
			name = name,
			token = token
		)
	}

	override suspend fun sendRegistrationEmail(email: String, name: String, token: String) {
		sendTokenEmail(
			templateId = config.registrationTemplateId,
			email = email,
			name = name,
			token = token
		)
	}

}