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
import org.testadirapa.sesterzo.api.BudgetApi
import org.testadirapa.sesterzo.api.BudgetElementApi
import org.testadirapa.sesterzo.api.SpaceApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.BudgetPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Budget.Companion.getBudgetId
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.toReference
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Duration

class BudgetApiImpl(
	httpConfig: HttpConfig,
	cache: BudgetPersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
	private val spaceApi: SpaceApi,
	private val budgetElementApi: BudgetElementApi
) : TemporizedCachedApi<EncryptedBudget, CachedBudget>(httpConfig, cache, ttl), BudgetApi {

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

	override suspend fun getBudget(spaceId: String, budgetId: String, bypassCache: Boolean): DecryptedBudget? = cachedOrGetIfPresent(
		id = budgetId,
		bypassCache = bypassCache,
	) { retrieveBudgetInSpace(spaceId, budgetId) }?.let {
		cryptoService.decrypt(it)
	}

	override suspend fun getOrCreateMonthBudget(
		spaceId: String,
		budgetDate: GMTDate,
		bypassCache: Boolean
	): DecryptedBudget {
		val year = budgetDate.year
		val month = budgetDate.month.ordinal + 1
		val id = getBudgetId(year = year, month = month)
		return getBudget(
			spaceId = spaceId,
			budgetId = id,
			bypassCache = bypassCache
		) ?: run {
			val space = spaceApi.getSpace(spaceId = spaceId, bypassCache = bypassCache)
			DecryptedBudget(
				year = year,
				month = month,
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
			).let {
				createBudget(
					spaceId = spaceId,
					budget = it
				)
			}
		}
	}

	override suspend fun createBudget(spaceId: String, budget: DecryptedBudget): DecryptedBudget = createBudgetInSpace(
		spaceId = spaceId,
		budget = cryptoService.encrypt(budget)
	).bodyOrThrow().let { encryptedBudget ->
		putInCache(encryptedBudget)
		cryptoService.decrypt(encryptedBudget)
	}
}