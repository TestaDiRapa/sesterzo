package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.EntryDAO
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Timestamp

class EntryDAOImpl(
	client: DBClient
) : EntryDAO(client) {

	companion object {
		private const val BY_BUDGET_UPDATE_INDEX_NAME = "by_budget_update"
	}

	override suspend fun initIndexes(spaceId: String) {
		val collection = getCollection(spaceId)
		if (collection.listIndexes().firstOrNull { it["name"] == BY_BUDGET_UPDATE_INDEX_NAME } == null) {
			collection.createIndex(
				Indexes.descending(EncryptedEntry::budgetId.name, EncryptedEntry::updated.name),
				IndexOptions().name(BY_BUDGET_UPDATE_INDEX_NAME),
			)
		}
	}

	override fun getEntriesForBudget(spaceId: String, budgetId: String): Flow<EncryptedEntry> =
		find(
			spaceId = spaceId,
			filter = Filters.eq(EncryptedEntry::budgetId.name, budgetId)
		)

	override fun getEntriesForBudgetAfter(
		spaceId: String,
		budgetId: String,
		after: Timestamp
	): Flow<EncryptedEntry> = find(
		spaceId = spaceId,
		filter = Filters.and(
			Filters.eq(EncryptedEntry::budgetId.name, budgetId),
			Filters.gte(EncryptedEntry::updated.name, after),
		)
	)

	override suspend fun softDeleteEntry(spaceId: String, entryId: String): EncryptedEntry? =
		getCollection(spaceId).findOneAndUpdate(
			filter = Filters.eq("_id", entryId),
			update = Updates.combine(
				Updates.set(EncryptedEntry::updated.name, System.currentTimeMillis()),
				Updates.set(EncryptedEntry::deleted.name, true)
			),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)

	override fun softDeleteEntries(spaceId: String, entryIds: List<String>): Flow<EncryptedEntry> = flow {
		getCollection(spaceId).updateMany(
			filter = Filters.`in`("_id", entryIds),
			update = Updates.combine(
				Updates.set(EncryptedEntry::updated.name, System.currentTimeMillis()),
				Updates.set(EncryptedEntry::deleted.name, true)
			)
		)
		emitAll(
			getByIds(
				spaceId = spaceId,
				ids = entryIds.toSet()
			)
		)
	}
}