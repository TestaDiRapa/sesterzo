package org.testadirapa.sesterzo.api.impl

import com.icure.kryptom.crypto.defaultCryptoService
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import org.testadirapa.sesterzo.api.CachedApi
import org.testadirapa.sesterzo.api.EntryApi
import org.testadirapa.sesterzo.api.UserApi
import org.testadirapa.sesterzo.cache.EntryPersistentCache
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrapPatching
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toBudgetId
import kotlin.collections.map
import kotlin.time.Clock

class EntryApiImpl(
	httpConfig: HttpConfig,
	cache: EntryPersistentCache,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
	private val userApi: UserApi,
) : CachedApi<EncryptedEntry, EncryptedEntry, EntryPersistentCache>(httpConfig, cache), EntryApi {

	private val lastUpdateForSpace = MutableStateFlow<Map<Pair<String, String>, Timestamp>>(emptyMap())
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
	}.wrapPatching { entries -> entries.map { it.copy(transientSpaceId = spaceId) } }

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
	}.wrapPatching { entries -> entries.map { it.copy(transientSpaceId = spaceId) } }

	private suspend fun createEntryInSpace(
		spaceId: String,
		entry: EncryptedEntry,
	): HttpResponse<EncryptedEntry> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(entry)
	}.wrapPatching { entry -> entry.copy(transientSpaceId = spaceId) }

	override suspend fun getInSpaceForBudget(spaceId: String, budgetId: String, bypassCache: Boolean): List<DecryptedEntry> {
		val cached = cache.getAllForBudgetInSpace(spaceId = spaceId, budgetId = budgetId).sortedByDescending { it.updated }
		return if (bypassCache || cached.isEmpty()) {
			retrieveAllInSpaceForBudget(spaceId = spaceId, budgetId = budgetId)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.map { cryptoService.decrypt(it) }
				.sortedByDescending { it.updated }
		} else {
			val lastRegistered = cached.first().updated
			val updates = retrieveAllInSpaceForBudgetAfter(spaceId = spaceId, budgetId = budgetId, after = lastRegistered)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.map { cryptoService.decrypt(it) }
				.associateBy { it.id }
			updates.values.sortedByDescending { it.updated } +
				cached.filter { !updates.containsKey(it.id) }.map { cryptoService.decrypt(it) }
		}.also {
			it.firstOrNull()?.let { latestEntry ->
				setLastUpdateReference(spaceId = spaceId, budgetId = budgetId, value = latestEntry.updated)
			}
		}
	}

	private suspend fun getAndCacheAfterLatestReference(spaceId: String, budgetId: String) {
		val lastRegisteredInCache = getLastUpdateReferenceOrNull(spaceId = spaceId, budgetId = budgetId)
		val lastRegistered = if (lastRegisteredInCache == null) {
			retrieveAllInSpaceForBudget(spaceId = spaceId, budgetId = budgetId)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.map { cryptoService.decrypt(it) }
				.maxByOrNull { it.updated }
		} else {
			retrieveAllInSpaceForBudgetAfter(spaceId = spaceId, budgetId = budgetId, after = lastRegisteredInCache)
				.bodyOrThrow()
				.onEach { putInCache(it) }
				.map { cryptoService.decrypt(it) }
				.maxByOrNull { it.updated }
		}
		if (lastRegistered != null) {
			setLastUpdateReference(spaceId = spaceId, budgetId = budgetId, value = lastRegistered.updated)
		}
	}

	private suspend fun getLastUpdateReferenceOrNull(spaceId: String, budgetId: String): Timestamp? =
		lastUpdateForSpace.updateAndGet { values ->
			if (!values.containsKey(spaceId to budgetId)) {
				val lastForSpaceBudget =
					cache.getAllForBudgetInSpace(spaceId = spaceId, budgetId = budgetId).maxByOrNull { it.updated }?.updated
				if (lastForSpaceBudget != null) {
					values + ((spaceId to budgetId) to lastForSpaceBudget)
				} else {
					values
				}
			} else {
				values
			}
		}[spaceId to budgetId]

	private fun setLastUpdateReference(spaceId: String, budgetId: String, value: Timestamp) {
		lastUpdateForSpace.update { values ->
			values + ((spaceId to budgetId) to value)
		}
	}

	override suspend fun createEntryInSpace(
		spaceId: String,
		budgetReference: BudgetReference,
		type: Entry.EntryType,
		label: String,
		amount: Amount,
		description: String?,
	): DecryptedEntry {
		val entry = DecryptedEntry(
			id = defaultCryptoService.strongRandom.randomUUID(),
			updated = Clock.System.now().toEpochMilliseconds(),
			deleted = false,
			budgetId = budgetReference.toBudgetId(),
			createdBy = userApi.getCurrentUser().id,
			deletedBy = null,
			type = type,
			label = label,
			amount = amount,
			description = description?.takeIf { it.isNotBlank() },
			spaceId = spaceId,
		)
		val createdEntry = createEntryInSpace(
			spaceId = spaceId,
			entry = cryptoService.encrypt(entry)
		).bodyOrThrow().let {
			cryptoService.decrypt(it)
		}
		getAndCacheAfterLatestReference(spaceId = spaceId, budgetId = budgetReference.toBudgetId())
		return createdEntry
	}
}