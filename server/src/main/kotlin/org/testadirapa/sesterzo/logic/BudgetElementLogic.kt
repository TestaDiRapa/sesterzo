package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.EncryptedBudgetElement

interface BudgetElementLogic {
	suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement
	suspend fun createBudgetElement(spaceId: String, budgetElement: EncryptedBudgetElement): EncryptedBudgetElement
	suspend fun getBudgetElement(spaceId: String, budgetElementId: String, version: Int): EncryptedBudgetElement
}