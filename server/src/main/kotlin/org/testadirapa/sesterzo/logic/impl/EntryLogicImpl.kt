package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.testadirapa.sesterzo.dao.EntryDAO
import org.testadirapa.sesterzo.exceptions.ExpenseDeletionFailedException
import org.testadirapa.sesterzo.logic.EntryLogic
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.utils.requireSizeIsUnderThreshold

class EntryLogicImpl(
	private val entryDAO: EntryDAO,
) : EntryLogic {

	override fun getEntriesForBudget(spaceId: String, budgetId: String): Flow<EncryptedEntry> =
		entryDAO.getEntriesForBudget(spaceId = spaceId, budgetId = budgetId)

	override fun getEntriesForBudgetAfter(
		spaceId: String,
		budgetId: String,
		after: Timestamp
	) : Flow<EncryptedEntry> = entryDAO.getEntriesForBudgetAfter(
		spaceId = spaceId,
		budgetId = budgetId,
		after = after
	)

	override suspend fun deleteEntry(spaceId: String, entryId: String): EncryptedEntry =
		entryDAO.softDeleteEntry(spaceId = spaceId, entryId = entryId)
			?: throw ExpenseDeletionFailedException(entryId)

	override suspend fun createEntry(spaceId: String, entry: EncryptedEntry): EncryptedEntry {
		entry.requireSizeIsUnderThreshold()
		return entryDAO.save(
			spaceId = spaceId,
			entity = entry,
		)
	}

	override fun updateBuiltInEntries(
		spaceId: String,
		entriesToCreate: List<EncryptedEntry>,
		entryIdsToDelete: List<String>,
	): Flow<EncryptedEntry> = flow {
		entriesToCreate.forEach { entry ->
			entry.requireSizeIsUnderThreshold()
		}
		if (entriesToCreate.isNotEmpty()) {
			emitAll(
				entryDAO.save(
					spaceId = spaceId,
					entities = entriesToCreate,
				)
			)
		}
		if (entryIdsToDelete.isNotEmpty()) {
			emitAll(
				entryDAO.softDeleteEntries(
					spaceId = spaceId,
					entryIds = entryIdsToDelete
				)
			)
		}
	}
}