package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.SpaceApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class SpaceApiImpl(
	httpConfig: HttpConfig,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : AbstractApi(httpConfig), SpaceApi {

	override val baseSegment: String = "space"
	private val spaceCacheDuration = 15.minutes
	private val spaceCache = mutableMapOf<String, Pair<Space, Timestamp>>()

	override suspend fun getSpaces(): List<Space> =
		retrieveSpaces().bodyOrThrow().onEach { space ->
			cryptoService.decryptAndLoadSpaceKey(space)
			spaceCache[space.id] = space to Clock.System.now().toEpochMilliseconds()
		}

	private suspend fun retrieveSpaces(): HttpResponse<List<Space>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()


}