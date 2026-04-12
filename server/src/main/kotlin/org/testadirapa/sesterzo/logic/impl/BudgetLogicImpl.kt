package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.BudgetLogic
import org.testadirapa.sesterzo.model.EncryptedBudget

class BudgetLogicImpl(
	private val budgetDAO: BudgetDAO
) : BudgetLogic {

	override suspend fun createBudget(
		spaceId: String,
		budget: EncryptedBudget
	): EncryptedBudget {
		require(budget.spaceId == spaceId) {
			"You cannot create a budget in a different space"
		}
		return budgetDAO.save(
			spaceId = spaceId,
			entity = budget
		)
	}

	override suspend fun getBudget(
		spaceId: String,
		budgetId: String
	): EncryptedBudget = budgetDAO.getById(spaceId = spaceId, id = budgetId)
		?: throw EntityNotFoundException(budgetId, ExceptionLabel.BudgetNotFound)
}