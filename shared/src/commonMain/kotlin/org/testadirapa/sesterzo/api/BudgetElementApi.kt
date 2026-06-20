package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.VersionableReference

interface BudgetElementApi {
	suspend fun getLatestBudgetElementById(spaceId: String, budgetElementId: String): DecryptedBudgetElement
	suspend fun createBudgetElement(spaceId: String, budgetElement: DecryptedBudgetElement): DecryptedBudgetElement
	suspend fun getBudgetElement(spaceId: String, budgetElementReference: VersionableReference): DecryptedBudgetElement
	suspend fun getBugetElements(
		spaceId: String,
		budgetElementReferences: List<VersionableReference>
	): List<DecryptedBudgetElement>
}