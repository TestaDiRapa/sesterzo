package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.EncryptedBudgetElement

interface BudgetElementLogic {
	suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement
}