package org.testadirapa.sesterzo.api

import kotlinx.datetime.LocalDate
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.utils.currentDate

interface BudgetApi {
	suspend fun getBudget(spaceId: String, budgetId: String, bypassCache: Boolean): DecryptedBudget?
	suspend fun createBudget(spaceId: String, budget: DecryptedBudget): DecryptedBudget
	suspend fun getOrCreateMonthBudget(
		spaceId: String,
		budgetDate: LocalDate = currentDate(),
		bypassCache: Boolean
	): DecryptedBudget

	suspend fun getBudgetsInSpaceForYear(spaceId: String, year: Int): List<DecryptedBudget>
}