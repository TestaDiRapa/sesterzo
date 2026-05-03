package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.model.EncryptedEntry

interface EntryPersistentCache : PersistenceOperator<EncryptedEntry, EncryptedEntry> {
	suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedEntry>
}