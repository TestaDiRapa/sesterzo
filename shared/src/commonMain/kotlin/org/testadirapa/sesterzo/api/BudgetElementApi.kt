package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.DecryptedBudgetElement

interface BudgetElementApi {
	suspend fun getLatestBudgetElementById(spaceId: String, budgetElementId: String): DecryptedBudgetElement
	suspend fun createBudgetElement(spaceId: String, budgetElement: DecryptedBudgetElement): DecryptedBudgetElement
}