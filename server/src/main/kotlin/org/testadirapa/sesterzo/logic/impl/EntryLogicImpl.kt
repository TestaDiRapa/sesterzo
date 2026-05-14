package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
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
}