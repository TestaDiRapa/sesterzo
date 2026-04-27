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
import kotlinx.datetime.number
import org.testadirapa.sesterzo.api.BudgetApi
import org.testadirapa.sesterzo.api.BudgetElementApi
import org.testadirapa.sesterzo.api.SpaceApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.BudgetPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.toReference
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toBudgetId
import kotlin.time.Duration

class BudgetApiImpl(
	httpConfig: HttpConfig,
	cache: BudgetPersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
	private val spaceApi: SpaceApi,
	private val budgetElementApi: BudgetElementApi
) : TemporizedCachedApi<EncryptedBudget, CachedBudget, BudgetPersistentCache>(httpConfig, cache, ttl), BudgetApi {

	override val baseSegment: String = "budget"

	private suspend fun retrieveBudgetInSpace(spaceId: String, budgetId: String): HttpResponse<EncryptedBudget> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, budgetId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun retrieveFirstBudgetAfter(spaceId: String, budgetReference: BudgetReference): HttpResponse<EncryptedBudget> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "after", budgetReference.year.toString(), budgetReference.month.number.toString())
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun retrieveFirstBudgetBefore(spaceId: String, budgetReference: BudgetReference): HttpResponse<EncryptedBudget> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "before", budgetReference.year.toString(), budgetReference.month.number.toString())
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun createBudgetInSpace(spaceId: String, budget: EncryptedBudget): HttpResponse<EncryptedBudget> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(budget)
	}.wrap()

	private suspend fun retrieveInSpaceForYear(spaceId: String, year: Int): HttpResponse<List<EncryptedBudget>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "forYear", "$year")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	override suspend fun getBudget(spaceId: String, budgetId: String, bypassCache: Boolean): DecryptedBudget? = cachedOrGetIfPresent(
		id = "$spaceId-$budgetId",
		bypassCache = bypassCache,
	) { retrieveBudgetInSpace(spaceId, budgetId) }?.let {
		cryptoService.decrypt(it)
	}

	override suspend fun getOrCreateMonthBudget(
		spaceId: String,
		budgetReference: BudgetReference,
		bypassCache: Boolean
	): DecryptedBudget = getBudget(
			spaceId = spaceId,
			budgetId = budgetReference.toBudgetId(),
			bypassCache = bypassCache
		) ?: createBudget(
			spaceId = spaceId,
			budgetReference = budgetReference
		)

	override suspend fun createBudget(spaceId: String, budgetReference: BudgetReference): DecryptedBudget {
		val space = spaceApi.getSpace(spaceId = spaceId, bypassCache = false)
		val budget = DecryptedBudget(
			year = budgetReference.year,
			month = budgetReference.month.number,
			version = 0,
			spaceId = spaceId,
			expensesReference = budgetElementApi.getLatestBudgetElementById(
				spaceId = spaceId,
				budgetElementId = space.fixedExpensesTemplateId
			).toReference(),
			incomeReference = budgetElementApi.getLatestBudgetElementById(
				spaceId = spaceId,
				budgetElementId = space.incomeSourcesTemplateId
			).toReference(),
			savingsReference = budgetElementApi.getLatestBudgetElementById(
				spaceId = spaceId,
				budgetElementId = space.savingsTemplateId
			).toReference(),
		)
		return createBudgetInSpace(
			spaceId = spaceId,
			budget = cryptoService.encrypt(budget)
		).bodyOrThrow().let { encryptedBudget ->
			putInCache(encryptedBudget)
			cryptoService.decrypt(encryptedBudget)
		}
	}

	override suspend fun getBudgetsInSpaceForYear(spaceId: String, year: Int, bypassCache: Boolean): List<DecryptedBudget> = getAllMergingIf(
		getFromCache = { cache.getByYearInSpace(spaceId = spaceId, year = year) },
		getFromNetwork = { retrieveInSpaceForYear(spaceId = spaceId, year = year) },
		bypassCacheCondition = { cached ->
			bypassCache || cached.map { it.month }.toSet() != setOf(1 .. 12)
		}
	).map { cryptoService.decrypt(it) }

	override suspend fun getFirstBudgetAfter(
		spaceId: String,
		budgetReference: BudgetReference,
		bypassCache: Boolean
	): DecryptedBudget? = cachedOrGetIfPresent(
		getFromCache = {
			cache.getFirstBudgetAfter(spaceId = spaceId, year = budgetReference.year, month = budgetReference.month.number)
		},
		bypassCache = bypassCache,
		getFromNetwork = { retrieveFirstBudgetAfter(spaceId = spaceId, budgetReference = budgetReference) }
	)?.let { cryptoService.decrypt(it) }

	override suspend fun getFirstBudgetBefore(
		spaceId: String,
		budgetReference: BudgetReference,
		bypassCache: Boolean
	): DecryptedBudget? = cachedOrGetIfPresent(
		getFromCache = {
			cache.getFirstBudgetBefore(spaceId = spaceId, year = budgetReference.year, month = budgetReference.month.number)
		},
		bypassCache = bypassCache,
		getFromNetwork = { retrieveFirstBudgetBefore(spaceId = spaceId, budgetReference = budgetReference) }
	)?.let { cryptoService.decrypt(it) }
}