package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Timestamp

abstract class EntryDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedEntry>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedEntry> = client.getCollection(spaceId)
	abstract fun getEntriesForBudget(spaceId: String, budgetId: String): Flow<EncryptedEntry>
	abstract fun getEntriesForBudgetAfter(spaceId: String, budgetId: String, after: Timestamp): Flow<EncryptedEntry>
	abstract suspend fun softDeleteEntry(spaceId: String, entryId: String): EncryptedEntry?
}
