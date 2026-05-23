package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface BudgetIntent : Intent {
	data object NavigateToPrevious : BudgetIntent
	data object NavigateToNext : BudgetIntent
	data class NavigateTo(val budgetReference: BudgetReference) : BudgetIntent
	data class CreateBudget(val newReference: BudgetReference): BudgetIntent
	data class UpdateCurrentBudgetTemplate(
		val type: BudgetElement.BudgetElementType,
		val reference: VersionableReference,
		val updateCurrentBudget: Boolean
	): BudgetIntent
}