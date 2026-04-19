package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.model.Timestamp

abstract class ExpenseDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedExpense>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedExpense> = client.getCollection(spaceId)
	abstract fun getExpensesForBudget(spaceId: String, budgetId: String): Flow<EncryptedExpense>
	abstract fun getExpensesForBudgetAfter(spaceId: String, budgetId: String, after: Timestamp): Flow<EncryptedExpense>
	abstract suspend fun softDeleteExpense(spaceId: String, expenseId: String): EncryptedExpense?
}
