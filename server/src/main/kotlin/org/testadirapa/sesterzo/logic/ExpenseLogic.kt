package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.model.Timestamp

interface ExpenseLogic {
	fun getExpensesForBudget(spaceId: String, budgetId: String): Flow<EncryptedExpense>
	fun getExpensesForBudgetAfter(spaceId: String, budgetId: String, after: Timestamp): Flow<EncryptedExpense>
	suspend fun deleteExpense(spaceId: String, expenseId: String): EncryptedExpense
}