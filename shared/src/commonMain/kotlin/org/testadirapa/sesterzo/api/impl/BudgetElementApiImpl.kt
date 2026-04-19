package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.BudgetElementApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.BudgetElementPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Duration

class BudgetElementApiImpl(
	httpConfig: HttpConfig,
	cache: BudgetElementPersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : TemporizedCachedApi<EncryptedBudgetElement, CachedBudgetElement, BudgetElementPersistentCache>(httpConfig, cache, ttl),
	BudgetElementApi {

	override val baseSegment: String = "budgetElement"

	private suspend fun retrieveLatestById(
		spaceId: String,
		budgetElementId: String
	): HttpResponse<EncryptedBudgetElement> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, budgetElementId, "latest")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	override suspend fun getLatestBudgetElementById(spaceId: String, budgetElementId: String): DecryptedBudgetElement =
		retrieveLatestById(spaceId = spaceId, budgetElementId = budgetElementId)
			.bodyOrThrow()
			.also { putInCache(it) }
			.let { cryptoService.decrypt(it) }
}