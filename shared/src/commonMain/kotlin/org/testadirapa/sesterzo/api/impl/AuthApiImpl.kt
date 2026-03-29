package org.testadirapa.sesterzo.api.impl

import com.icure.kerberus.Solution
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.model.dto.CaptchaChallenge
import org.testadirapa.sesterzo.model.dto.CompleteRegistrationData
import org.testadirapa.sesterzo.model.dto.LoginData
import org.testadirapa.sesterzo.model.dto.OttData
import org.testadirapa.sesterzo.model.dto.StartRegistrationData

class AuthApiImpl(
	config: HttpConfig
): AbstractApi(config = config), AuthApi {

	private val baseSegment = "auth"

	override suspend fun getCaptchaChallenge(
		input: String
	): HttpResponse<CaptchaChallenge> =
		get {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "captcha", input)
				parameter("ts", GMTDate().timestamp)
			}
			accept(Application.Json)
		}.wrap()

	override suspend fun startRegistration(
		registrationData: StartRegistrationData
	): HttpResponse<String> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "startRegistration")
			}
			accept(Application.Json)
			setBody(registrationData)
		}.wrap()

	override suspend fun completeRegistration(
		registrationProcessId: String,
		token: String
	): HttpResponse<AuthResponse> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "completeRegistration")
			}
			accept(Application.Json)
			setBody(
				CompleteRegistrationData(
					processId = registrationProcessId,
					token = token,
				)
			)
		}.wrap()

	override suspend fun getOtt(
		email: String,
		captchaSolution: Solution
	): HttpResponse<Unit> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "ott")
			}
			accept(Application.Json)
			setBody(
				OttData(
					email = email,
					captchaSolution = captchaSolution,
				)
			)
		}.wrap()

	override suspend fun login(
		email: String,
		token: String
	): HttpResponse<AuthResponse> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "login")
			}
			accept(Application.Json)
			setBody(
				LoginData(
					email = email,
					token = token,
				)
			)
		}.wrap()

	override suspend fun refresh(refreshToken: String): HttpResponse<AuthResponse> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "refresh")
			}
			accept(Application.Json)
			bearerAuth(refreshToken)
		}.wrap()
}