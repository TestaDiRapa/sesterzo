package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget
import db.CachedBudget as BudgetRow
import org.testadirapa.sesterzo.serialization.Serialization

class AndroidBudgetPersistentCache(
	driver: SqlDriver
) : BudgetPersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.budgetQueries

	private fun upsertInternal(budget: EncryptedBudget) {
		queries.upsert(
			id = "${budget.spaceId}-${budget.id}",
			version = budget.version.toLong(),
			spaceId = budget.spaceId,
			year = budget.year.toLong(),
			month = budget.month.toLong(),
			expensesReference = Serialization.json.encodeToString(budget.expensesReference),
			incomeReference = Serialization.json.encodeToString(budget.incomeReference),
			savingsReference = Serialization.json.encodeToString(budget.savingsReference),
			encryptedSelf = budget.encryptedSelf,
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: BudgetRow): CachedBudget = CachedBudget(
		id = row.id,
		entity = EncryptedBudget(
			version = row.version.toInt(),
			spaceId = row.spaceId,
			year = row.year.toInt(),
			month = row.month.toInt(),
			expensesReference = Serialization.json.decodeFromString(row.expensesReference),
			incomeReference = Serialization.json.decodeFromString(row.incomeReference),
			savingsReference = Serialization.json.decodeFromString(row.savingsReference),
			encryptedSelf = row.encryptedSelf,
		),
		insertedAt = row.inserted_at
	)

	override suspend fun upsert(entity: EncryptedBudget) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<EncryptedBudget>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<CachedBudget> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: EncryptedBudget) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): CachedBudget? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}