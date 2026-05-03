package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.CachedApi
import org.testadirapa.sesterzo.api.EntryApi
import org.testadirapa.sesterzo.cache.EntryPersistentCache
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrapPatching
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.collections.map

class EntryApiImpl(
	httpConfig: HttpConfig,
	cache: EntryPersistentCache,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : CachedApi<EncryptedEntry, EncryptedEntry, EntryPersistentCache>(httpConfig, cache), EntryApi {

	override val baseSegment: String = "entry"
	override fun convert(data: EncryptedEntry): EncryptedEntry = data

	private suspend fun retrieveAllInSpaceForBudget(
		spaceId: String,
		budgetId: String
	): HttpResponse<List<EncryptedEntry>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "forBudget", budgetId, "all")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrapPatching { expenses -> expenses.map { it.copy(transientSpaceId = spaceId) } }

	private suspend fun retrieveAllInSpaceForBudgetAfter(
		spaceId: String,
		budgetId: String,
		after: Timestamp
	): HttpResponse<List<EncryptedEntry>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "forBudget", budgetId, "after", "$after")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrapPatching { expenses -> expenses.map { it.copy(transientSpaceId = spaceId) } }

	override suspend fun getInSpaceForBudget(spaceId: String, budgetId: String, bypassCache: Boolean): List<DecryptedEntry> {
		val cached = cache.getAllForBudgetInSpace(spaceId = spaceId, budgetId = budgetId).sortedByDescending { it.updated }
		return if (bypassCache || cached.isEmpty()) {
			retrieveAllInSpaceForBudget(spaceId = spaceId, budgetId = budgetId)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.filterNot { it.deleted }
				.map { cryptoService.decrypt(it) }
				.sortedByDescending { it.updated }
		} else {
			val lastRegistered = cached.first().updated
			val updates = retrieveAllInSpaceForBudgetAfter(spaceId = spaceId, budgetId = budgetId, after = lastRegistered)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.map { cryptoService.decrypt(it) }
				.associateBy { it.id }
			updates.values
				.filterNot { it.deleted }
				.sortedByDescending { it.updated } +

				cached.filter {
					!it.deleted && !updates.containsKey(it.id)
				}.map { cryptoService.decrypt(it) }
		}
	}

}