package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.EntityUpdateFailedException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.BudgetLogic
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference

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

	override fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget> =
		budgetDAO.getBudgetsForYear(spaceId = spaceId, year = year)

	override suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): EncryptedBudget =
		budgetDAO.getFirstBudgetAfter(spaceId = spaceId, year = year, month = month)
			?: throw EntityNotFoundException("Budget after $year $month", ExceptionLabel.BudgetNotFound)

	override suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): EncryptedBudget =
		budgetDAO.getFirstBudgetBefore(spaceId = spaceId, year = year, month = month)
			?: throw EntityNotFoundException("Budget before $year $month", ExceptionLabel.BudgetNotFound)

	override suspend fun updateTemplateVersion(
		spaceId: String,
		budgetId: String,
		budgetVersion: Int,
		type: BudgetElement.BudgetElementType,
		budgetElementReference: VersionableReference
	): EncryptedBudget = budgetDAO.updateTemplateVersion(
		spaceId = spaceId,
		budgetId = budgetId,
		budgetVersion = budgetVersion,
		fieldName = when(type) {
			BudgetElement.BudgetElementType.FixedExpenses -> EncryptedBudget::expensesReference.name
			BudgetElement.BudgetElementType.Savings -> EncryptedBudget::savingsReference.name
			BudgetElement.BudgetElementType.Income -> EncryptedBudget::incomeReference.name
		},
		budgetElementReference = budgetElementReference
	) ?: throw EntityUpdateFailedException(
		entityId = budgetId,
		label = ExceptionLabel.BudgetElementNotFound
	)
}