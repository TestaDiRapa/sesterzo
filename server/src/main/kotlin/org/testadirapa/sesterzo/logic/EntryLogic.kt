package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Timestamp

interface EntryLogic {
	fun getEntriesForBudget(spaceId: String, budgetId: String): Flow<EncryptedEntry>
	fun getEntriesForBudgetAfter(spaceId: String, budgetId: String, after: Timestamp): Flow<EncryptedEntry>
	suspend fun deleteEntry(spaceId: String, entryId: String): EncryptedEntry
	suspend fun createEntry(spaceId: String, entry: EncryptedEntry): EncryptedEntry
	fun updateBuiltInEntries(
		spaceId: String,
		entriesToCreate: List<EncryptedEntry>,
		entryIdsToDelete: List<String>
	): Flow<EncryptedEntry>
}