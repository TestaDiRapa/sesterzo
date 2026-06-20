package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.EncryptedBudgetElement

interface BudgetElementLogic {
	suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement
	suspend fun createBudgetElement(spaceId: String, budgetElement: EncryptedBudgetElement): EncryptedBudgetElement
	suspend fun getBudgetElement(spaceId: String, budgetElementId: String, version: Int): EncryptedBudgetElement
	fun getBudgetElements(
		spaceId: String,
		budgetElementIds: List<String>
	): Flow<EncryptedBudgetElement>
}