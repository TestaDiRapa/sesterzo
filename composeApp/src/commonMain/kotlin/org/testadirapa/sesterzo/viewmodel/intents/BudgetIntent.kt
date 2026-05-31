package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface BudgetIntent : Intent {
	data object NavigateToPrevious : BudgetIntent
	data object NavigateToNext : BudgetIntent
	data class NavigateTo(val budgetReference: BudgetReference) : BudgetIntent
	data class CreateBudget(val newReference: BudgetReference): BudgetIntent
	data class UpdateCurrentBudgetTemplate(
		val type: BudgetElement.BudgetElementType,
		val budgetElement: DecryptedBudgetElement,
		val updateCurrentBudget: Boolean
	): BudgetIntent
	data class UpdateBudgetAmount(
		val budget: DecryptedBudget,
		val newAmounts: Map<String, Amount>,
		val type : Entry.EntryType,
	): BudgetIntent
}