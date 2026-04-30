package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface BudgetIntent : Intent {
	data object NavigateToPrevious : BudgetIntent
	data object NavigateToNext : BudgetIntent
	data class NavigateTo(val budgetReference: BudgetReference) : BudgetIntent
	data class CreateBudget(val newReference: BudgetReference): BudgetIntent
}