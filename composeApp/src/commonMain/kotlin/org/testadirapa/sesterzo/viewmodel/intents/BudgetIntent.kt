package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface BudgetIntent : Intent {
	data object NavigateToPrevious : BudgetIntent
	data object NavigateToNext : BudgetIntent
	data class NavigateTo(val budgetReference: BudgetReference) : BudgetIntent
	data class CreateBudget(val newReference: BudgetReference): BudgetIntent
	data class CreateEntry(
		val budgetReference: BudgetReference,
		val type: Entry.EntryType,
		val label: String,
		val amount: Amount,
		val description: String?
	): BudgetIntent
	data class DeleteEntry(val entryId: String): BudgetIntent
}