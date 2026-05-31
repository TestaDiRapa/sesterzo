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
import org.testadirapa.sesterzo.model.dto.BulkEntryUpdate
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toBudgetId
import org.testadirapa.sesterzo.utils.toTimestampOfStartValidity
import kotlin.collections.map
import kotlin.time.Clock

class EntryApiImpl(
	httpConfig: HttpConfig,
	cache: EntryPersistentCache,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
	private val userApi: UserApi,
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
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(entry)
	}.wrapPatching { entry -> entry.copy(transientSpaceId = spaceId) }

	private suspend fun updateBulkEntriesInSpace(
		spaceId: String,
		entriesToCreate: List<EncryptedEntry>,
		entryIdsToDelete: List<String>,
	): HttpResponse<List<EncryptedEntry>> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, "bulk")
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(BulkEntryUpdate(entriesToCreate, entryIdsToDelete))
	}.wrapPatching { entries -> entries.map { it.copy(transientSpaceId = spaceId) } }

	private suspend fun deleteEntryInSpace(
		spaceId: String,
		entryId: String,
	): HttpResponse<EncryptedEntry> = delete {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, entryId)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
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
		}
	}

	override suspend fun createEntryAndRetrieve(
		spaceId: String,
		budgetReference: BudgetReference,
		type: Entry.EntryType,
		label: String,
		amount: Amount,
		description: String?,
	): List<DecryptedEntry> {
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
		createEntryInSpace(
			spaceId = spaceId,
			entry = cryptoService.encrypt(entry)
		).bodyOrThrow()
		return getInSpaceForBudget(spaceId = spaceId, budgetId = budgetReference.toBudgetId(), bypassCache = false)
	}

	override suspend fun createEntriesAndRetrieve(
		spaceId: String,
		budgetReference: BudgetReference,
		entries: List<DecryptedEntry>
	): List<DecryptedEntry> {
		if (entries.isNotEmpty()) {
			updateBulkEntriesInSpace(
				spaceId = spaceId,
				entriesToCreate = entries.map {
					cryptoService.encrypt(it)
				},
				entryIdsToDelete = emptyList(),
			).bodyOrThrow()
		}
		return getInSpaceForBudget(spaceId = spaceId, budgetId = budgetReference.toBudgetId(), bypassCache = false)
	}

	override suspend fun createOrUpdateBuiltInEntries(
		spaceId: String,
		budgetReference: BudgetReference,
		type: Entry.EntryType,
		entries: Map<String, Amount>,
	) {
		val currentEntries = getInSpaceForBudget(
			spaceId = spaceId,
			budgetId = budgetReference.toBudgetId(),
			bypassCache = false
		)
		val entriesToCreate = mutableListOf<DecryptedEntry>()
		val entryIdsToDelete = mutableListOf<String>()
		val currentEntriesByLabel = currentEntries.filter { !it.deleted && it.type == type }.associateBy { it.label }
		val budgetTimestamp = budgetReference.toTimestampOfStartValidity()
		val now = Clock.System.now().toEpochMilliseconds()
		entries.forEach { (label, amount) ->
			when {
				!currentEntriesByLabel.containsKey(label) -> {
					val entry = DecryptedEntry(
						id = defaultCryptoService.strongRandom.randomUUID(),
						updated = now.coerceAtLeast(budgetTimestamp),
						deleted = false,
						budgetId = budgetReference.toBudgetId(),
						createdBy = userApi.getCurrentUser().id,
						deletedBy = null,
						type = type,
						label = label,
						amount = amount,
						description = null,
						spaceId = spaceId,
						automation = true
					)
					entriesToCreate.add(entry)
				}
				currentEntriesByLabel.containsKey(label) &&
					currentEntriesByLabel.getValue(label).automation &&
					currentEntriesByLabel.getValue(label).amount != amount -> {
						val existingEntry = currentEntriesByLabel.getValue(label)
						entriesToCreate.add(
							existingEntry.copy(
								id = defaultCryptoService.strongRandom.randomUUID(),
								updated = now.coerceAtLeast(budgetTimestamp),
								amount = amount,
							)
						)
						entryIdsToDelete.add(existingEntry.id)
					}
			}
		}
		if (entriesToCreate.isNotEmpty() || entryIdsToDelete.isNotEmpty()) {
			println(budgetReference.toBudgetId())
			println(entriesToCreate)
			updateBulkEntriesInSpace(
				spaceId = spaceId,
				entriesToCreate = entriesToCreate.map {
					cryptoService.encrypt(it)
				},
				entryIdsToDelete = entryIdsToDelete,
			).bodyOrThrow().onEach {
				putInCache(it)
			}
		}
	}

	override suspend fun deleteEntryAndRetrieve(
		spaceId: String,
		entryId: String
	): List<DecryptedEntry> {
		val deletedEntry = deleteEntryInSpace(spaceId = spaceId, entryId = entryId).bodyOrThrow()
		return getInSpaceForBudget(spaceId = spaceId, budgetId = deletedEntry.budgetId, bypassCache = false)
	}
}