package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.SpaceApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.SpacePersistentCache
import org.testadirapa.sesterzo.cache.model.CachedSpace
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Duration

class SpaceApiImpl(
	httpConfig: HttpConfig,
	cache: SpacePersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : TemporizedCachedApi<Space, CachedSpace>(httpConfig, cache, ttl), SpaceApi {

	override val baseSegment: String = "space"

	private suspend fun retrieveSpaces(): HttpResponse<List<Space>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	override suspend fun getSpaces(): List<Space> = getAllMerging {
		retrieveSpaces()
	}

}