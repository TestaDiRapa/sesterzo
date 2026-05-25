package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.exceptions.BudgetConflictException
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.BudgetLogic
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.model.dto.BulkOperationElementResult
import org.testadirapa.sesterzo.utils.requireSizeIsUnderThreshold

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
		budget.requireSizeIsUnderThreshold()
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

	override fun updateTemplateVersionOnBudgets(
		spaceId: String,
		budgetId: String,
		inclusiveStart: Boolean,
		type: BudgetElement.BudgetElementType,
		budgetElementReference: VersionableReference
	): Flow<BulkOperationElementResult<EncryptedBudget>> = flow {
		emitAll(
			budgetDAO.updateTemplatesVersionOnBudgets(
				spaceId = spaceId,
				budgetId = budgetId,
				inclusiveStart = inclusiveStart,
				fieldName = when(type) {
					BudgetElement.BudgetElementType.FixedExpenses -> EncryptedBudget::expensesReference.name
					BudgetElement.BudgetElementType.Savings -> EncryptedBudget::savingsReference.name
					BudgetElement.BudgetElementType.Income -> EncryptedBudget::incomeReference.name
				},
				updatedField = {
					when(type) {
						BudgetElement.BudgetElementType.FixedExpenses -> expensesReference
						BudgetElement.BudgetElementType.Savings -> savingsReference
						BudgetElement.BudgetElementType.Income -> incomeReference
					}
				},
				budgetElementReference = budgetElementReference
			)
		)
	}

	override suspend fun setEncryptedSelfOnBudget(
		spaceId: String,
		budget: EncryptedBudget
	): EncryptedBudget = budgetDAO.setEncryptedSelfOnBudget(
		spaceId = spaceId,
		budgetId = budget.id,
		budgetVersion = budget.version,
		encryptedSelf = budget.encryptedSelf
	) ?: throw BudgetConflictException(budgetId = budget.id)
}