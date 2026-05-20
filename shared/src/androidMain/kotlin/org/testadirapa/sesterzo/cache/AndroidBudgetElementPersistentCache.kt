package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import db.CachedBudgetElement as BudgetElementRow

class AndroidBudgetElementPersistentCache(
	driver: SqlDriver
) : BudgetElementPersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.budgetElementQueries

	private fun upsertInternal(budget: EncryptedBudgetElement) {
		queries.upsert(
			id = budget.id,
			budgetElementId = budget.budgetElementId,
			version = budget.version.toLong(),
			spaceId = budget.spaceId,
			type = budget.type.name,
			encryptedSelf = budget.encryptedSelf,
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: BudgetElementRow): CachedBudgetElement = CachedBudgetElement(
		id = row.id,
		entity = EncryptedBudgetElement(
			budgetElementId = row.budgetElementId,
			version = row.version.toInt(),
			spaceId = row.spaceId,
			type = BudgetElement.BudgetElementType.valueOf(row.type),
			encryptedSelf = row.encryptedSelf,
		),
		insertedAt = row.inserted_at
	)

	override suspend fun upsert(entity: EncryptedBudgetElement) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<EncryptedBudgetElement>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<CachedBudgetElement> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: EncryptedBudgetElement) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): CachedBudgetElement? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun getByIds(ids: List<String>): List<CachedBudgetElement> = withContext(Dispatchers.IO) {
		queries.selectByIds(ids).executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}