package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.model.EncryptedEntry
import db.Entry as EntryRow

class AndroidEntryPersistentCache(
	driver: SqlDriver
) : EntryPersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.entryQueries

	private fun upsertInternal(expense: EncryptedEntry) {
		queries.upsert(
			id = expense.id,
			updated = expense.updated,
			deleted = if (expense.deleted) 1L else 0L,
			budgetId = expense.budgetId,
			spaceId = expense.spaceId,
			encryptedSelf = expense.encryptedSelf
		)
	}

	private fun rowToEntity(row: EntryRow): EncryptedEntry =
		EncryptedEntry(
			id = row.id,
			updated = row.updated,
			deleted = row.deleted == 1L,
			budgetId = row.budgetId,
			transientSpaceId = row.spaceId,
			encryptedSelf = row.encryptedSelf
		)

	override suspend fun upsert(entity: EncryptedEntry) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<EncryptedEntry>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<EncryptedEntry> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: EncryptedEntry) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): EncryptedEntry? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}

	override suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedEntry> = withContext(Dispatchers.IO) {
		queries.selectBySpaceBudget(budgetId = budgetId, spaceId = spaceId).executeAsList().map { rowToEntity(it) }
	}
}