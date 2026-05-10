package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference

interface BudgetLogic {

	suspend fun createBudget(spaceId: String, budget: EncryptedBudget): EncryptedBudget
	suspend fun getBudget(spaceId: String, budgetId: String): EncryptedBudget
	fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget>
	suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): EncryptedBudget
	suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): EncryptedBudget
	suspend fun updateTemplateVersion(
		spaceId: String,
		budgetId: String,
		budgetVersion: Int,
		type: BudgetElement.BudgetElementType,
		budgetElementReference: VersionableReference
	): EncryptedBudget
}