package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.EncryptedBudget

interface BudgetLogic {

	suspend fun createBudget(spaceId: String, budget: EncryptedBudget): EncryptedBudget
	suspend fun getBudget(spaceId: String, budgetId: String): EncryptedBudget
	fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget>
}