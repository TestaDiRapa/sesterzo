package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.model.EncryptedExpense
import db.Expense as ExpenseRow

class AndroidExpensePersistentCache(
	driver: SqlDriver
) : ExpensePersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.expenseQueries

	private fun upsertInternal(expense: EncryptedExpense) {
		queries.upsert(
			id = expense.id,
			updated = expense.updated,
			deleted = if (expense.deleted) 1L else 0L,
			budgetId = expense.budgetId,
			spaceId = checkNotNull(expense.spaceId) { "Cannot cache an Expense without spaceId" },
			encryptedSelf = expense.encryptedSelf
		)
	}

	private fun rowToEntity(row: ExpenseRow): EncryptedExpense =
		EncryptedExpense(
			id = row.id,
			updated = row.updated,
			deleted = row.deleted == 1L,
			budgetId = row.budgetId,
			spaceId = row.spaceId,
			encryptedSelf = row.encryptedSelf
		)

	override suspend fun upsert(entity: EncryptedExpense) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<EncryptedExpense>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<EncryptedExpense> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: EncryptedExpense) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): EncryptedExpense? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}

	override suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedExpense> = withContext(Dispatchers.IO) {
		queries.selectBySpaceBudget(budgetId = budgetId, spaceId = spaceId).executeAsList().map { rowToEntity(it) }
	}
}