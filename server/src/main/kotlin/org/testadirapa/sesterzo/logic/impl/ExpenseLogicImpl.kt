package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.dao.ExpenseDAO
import org.testadirapa.sesterzo.exceptions.ExpenseDeletionFailedException
import org.testadirapa.sesterzo.logic.ExpenseLogic
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.model.Timestamp

class ExpenseLogicImpl(
	private val expenseDAO: ExpenseDAO,
) : ExpenseLogic {

	override fun getExpensesForBudget(spaceId: String, budgetId: String): Flow<EncryptedExpense> =
		expenseDAO.getExpensesForBudget(spaceId = spaceId, budgetId = budgetId)

	override fun getExpensesForBudgetAfter(
		spaceId: String,
		budgetId: String,
		after: Timestamp
	) : Flow<EncryptedExpense> = expenseDAO.getExpensesForBudgetAfter(
		spaceId = spaceId,
		budgetId = budgetId,
		after = after
	)

	override suspend fun deleteExpense(spaceId: String, expenseId: String): EncryptedExpense =
		expenseDAO.softDeleteExpense(spaceId = spaceId, expenseId = expenseId)
			?: throw ExpenseDeletionFailedException(expenseId)
}