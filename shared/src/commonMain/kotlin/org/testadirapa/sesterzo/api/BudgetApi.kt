package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.currentBudgetReference

interface BudgetApi {
	suspend fun getBudget(spaceId: String, budgetId: String, bypassCache: Boolean): DecryptedBudget?
	suspend fun createBudget(spaceId: String, budgetReference: BudgetReference): DecryptedBudget
	suspend fun getOrCreateMonthBudget(
		spaceId: String,
		budgetReference: BudgetReference = currentBudgetReference(),
		bypassCache: Boolean
	): DecryptedBudget

	suspend fun getBudgetsInSpaceForYear(spaceId: String, year: Int, bypassCache: Boolean): List<DecryptedBudget>
	suspend fun getFirstBudgetAfter(
		spaceId: String,
		budgetReference: BudgetReference,
		bypassCache: Boolean
	): DecryptedBudget?

	suspend fun getFirstBudgetBefore(
		spaceId: String,
		budgetReference: BudgetReference,
		bypassCache: Boolean
	): DecryptedBudget?

	suspend fun updateBudgetTemplate(
		spaceId: String,
		budgetReference: BudgetReference,
		type: BudgetElement.BudgetElementType,
		budgetElementReference: VersionableReference
	)
}