package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.budget.BudgetSelector
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.components.mobile.entries.AddEntryButtonWithForm
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.screens.main.mobile.entries.MobileEntriesScreen
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.EntriesViewModel
import org.testadirapa.sesterzo.viewmodel.intents.EntryIntent

@Composable
fun MobileBudgetScreen(
	space: Space,
	page: Page,
	budget: BudgetViewModel.BudgetWithTemplates,
	budgetLoadingState: Boolean,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onBudgetUpdate: (budget: DecryptedBudget, newAmounts: Map<String, Amount>, type: Entry.EntryType) -> Unit,
	onError: (e: Throwable) -> Unit,
) {
	val viewModel = viewModel(key = "${space.id}-${budget.budget.id}-${budget.budget.version}") {
		EntriesViewModel(
			spaceId = space.id,
			budget = budget.budget,
			errorHandler = onError
		)
	}
	val entries = viewModel.entriesViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()
	Scaffold(
		topBar = {
			BudgetSelector(
				space = space,
				budget = budget.budget,
				onPreviousBudget = onPreviousBudget,
				onNextBudget = onNextBudget,
				onMonthSelect = onMonthSelect,
				onCreateBudget = onCreateBudget,
				onError = onError,
				modifier = Modifier.fillMaxWidth()
			)
		},
		floatingActionButton = {
			AddEntryButtonWithForm(
				space = space,
				currentBudget = budget.budget.toReference(),
				loadingState = loadingState.value,
				onCreate = { budgetReference, date, type, label, amount, description ->
					viewModel.acceptIntent(
						EntryIntent.CreateEntry(
							budgetReference = budgetReference,
							date = date,
							type = type,
							label = label,
							amount = amount,
							description = description,
						)
					)
				}
			)
		},
		floatingActionButtonPosition = FabPosition.End,
	) { scaffoldPadding ->
		Column {
			when (page) {
				Page.Budget -> {
					BudgetComponent(
						scaffoldPadding = scaffoldPadding,
						entries = entries.value,
						space = space,
						budget = budget,
						budgetLoadingState = budgetLoadingState,
						loadingState = loadingState.value,
						onCreateEntry = { budgetReference, date, type, label, amount, description ->
							viewModel.acceptIntent(
								EntryIntent.CreateEntry(
									budgetReference = budgetReference,
									date = date,
									type = type,
									label = label,
									amount = amount,
									description = description,
								)
							)
						},
						onBudgetUpdate = onBudgetUpdate
					)
				}
				Page.Entries -> {
					MobileEntriesScreen(
						scaffoldPadding = scaffoldPadding,
						entries = entries.value,
						onDelete = { entryId ->
							viewModel.acceptIntent(EntryIntent.DeleteEntry(entryId))
						}
					)
				}
				else -> {}
			}
		}
	}
}