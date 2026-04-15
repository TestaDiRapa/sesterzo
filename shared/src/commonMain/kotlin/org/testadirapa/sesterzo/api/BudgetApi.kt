package org.testadirapa.sesterzo.api

import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.model.DecryptedBudget

interface BudgetApi {
	suspend fun getBudget(spaceId: String, budgetId: String, bypassCache: Boolean): DecryptedBudget?
	suspend fun createBudget(spaceId: String, budget: DecryptedBudget): DecryptedBudget
	suspend fun getOrCreateMonthBudget(
		spaceId: String,
		budgetDate: GMTDate = GMTDate(),
		bypassCache: Boolean
	): DecryptedBudget
}