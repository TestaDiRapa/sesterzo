package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface SpaceIntent : Intent {
	data object NavigateToPrevious : SpaceIntent
	data object NavigateToNext : SpaceIntent
	data class NavigateTo(val budgetReference: BudgetReference) : SpaceIntent
	data class CreateBudget(val newReference: BudgetReference): SpaceIntent
	data class CreateEntry(
		val budgetReference: BudgetReference,
		val type: Entry.EntryType,
		val label: String,
		val amount: Amount,
		val description: String?
	): SpaceIntent
}