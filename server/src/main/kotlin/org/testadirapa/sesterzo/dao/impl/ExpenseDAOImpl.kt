package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.ExpenseDAO
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.model.User

class ExpenseDAOImpl(
	client: DBClient
) : ExpenseDAO(client) {

	companion object {
		private const val BY_BUDGET_UPDATE_INDEX_NAME = "by_budget_update"
	}

	override suspend fun initIndexes(spaceId: String) {
		val collection = getCollection(spaceId)
		if (collection.listIndexes().firstOrNull { it["name"] == BY_BUDGET_UPDATE_INDEX_NAME } == null) {
			collection.createIndex(
				Indexes.descending(EncryptedExpense::budgetId.name, EncryptedExpense::updated.name),
				IndexOptions().name(BY_BUDGET_UPDATE_INDEX_NAME),
			)
		}
	}

	override fun getExpensesForBudget(spaceId: String, budgetId: String): Flow<EncryptedExpense> =
		find(
			spaceId = spaceId,
			filter = Filters.eq(EncryptedExpense::budgetId.name, budgetId)
		)

	override fun getExpensesForBudgetAfter(
		spaceId: String,
		budgetId: String,
		after: Timestamp
	): Flow<EncryptedExpense> = find(
		spaceId = spaceId,
		filter = Filters.and(
			Filters.eq(EncryptedExpense::budgetId.name, budgetId),
			Filters.gte(EncryptedExpense::updated.name, after),
		)
	)

	override suspend fun softDeleteExpense(spaceId: String, expenseId: String): EncryptedExpense? =
		getCollection(spaceId).findOneAndUpdate(
			filter = Filters.eq("_id", expenseId),
			update = Updates.combine(
				Updates.set(EncryptedExpense::updated.name, System.currentTimeMillis()),
				Updates.set(EncryptedExpense::deleted.name, true)
			),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)
}