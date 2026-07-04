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
import org.testadirapa.sesterzo.api.ErrorReportApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.ErrorReport
import org.testadirapa.sesterzo.services.AuthService

class ErrorReportApiImpl(
	httpConfig: HttpConfig,
	private val authService: AuthService,
) : AbstractApi(httpConfig), ErrorReportApi {

	override val baseSegment: String = "error"

	private suspend fun createReport(errorReport: ErrorReport): HttpResponse<ErrorReport> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(errorReport)
	}.wrap()

	override suspend fun sendErrorReport(errorReport: ErrorReport) {
		createReport(errorReport).bodyOrNull()
	}

}