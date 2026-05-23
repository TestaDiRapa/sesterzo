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
import org.testadirapa.sesterzo.api.BudgetElementApi
import org.testadirapa.sesterzo.api.CachedApi
import org.testadirapa.sesterzo.cache.BudgetElementPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService

class BudgetElementApiImpl(
	httpConfig: HttpConfig,
	cache: BudgetElementPersistentCache,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : CachedApi<EncryptedBudgetElement, CachedBudgetElement, BudgetElementPersistentCache>(httpConfig, cache),
	BudgetElementApi {

	override val baseSegment: String = "budgetElement"

	override fun convert(data: CachedBudgetElement): EncryptedBudgetElement = data.entity

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

	private suspend fun retrieveBudgetElementById(
		spaceId: String,
		budgetElementId: String,
		version: Int,
	): HttpResponse<EncryptedBudgetElement> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, budgetElementId, "$version")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()


	private suspend fun saveBudgetElement(
		spaceId: String,
		budgetElement: EncryptedBudgetElement
	): HttpResponse<EncryptedBudgetElement> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(budgetElement)
	}.wrap()

	override suspend fun getLatestBudgetElementById(spaceId: String, budgetElementId: String): DecryptedBudgetElement =
		retrieveLatestById(spaceId = spaceId, budgetElementId = budgetElementId)
			.bodyOrThrow()
			.also { putInCache(it) }
			.let { cryptoService.decrypt(it) }

	override suspend fun getBudgetElement(
		spaceId: String,
		budgetElementReference: VersionableReference
	): DecryptedBudgetElement = cachedOrGet(
		id = budgetElementReference.toId(),
		bypassCache = false
	) {
		retrieveBudgetElementById(spaceId = spaceId, budgetElementId = budgetElementReference.id, version = budgetElementReference.version)
	}.let {
		cryptoService.decrypt(it)
	}

	override suspend fun createBudgetElement(
		spaceId: String,
		budgetElement: DecryptedBudgetElement
	): DecryptedBudgetElement = saveBudgetElement(
		spaceId = spaceId,
		budgetElement = cryptoService.encrypt(budgetElement)
	).bodyOrThrow().also {
		putInCache(it)
	}.let {
		cryptoService.decrypt(it)
	}
}