package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.mobile.budget.BudgetCalendarSelector
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.components.mobile.budget.BudgetMonthSelector
import org.testadirapa.sesterzo.components.mobile.entries.AddEntryButtonWithForm
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.screens.main.mobile.entries.MobileEntriesScreen
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.EntriesViewModel
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent
import org.testadirapa.sesterzo.viewmodel.intents.EntryIntent

@Composable
fun MobileBudgetScreen(
	space: Space,
	page: Page,
	budget: DecryptedBudget,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onError: (e: Throwable) -> Unit,
) {
	var calendarOpen by remember { mutableStateOf(false) }
	val viewModel = viewModel(key = "${space.id}-${budget.id}") {
		EntriesViewModel(
			spaceId = space.id,
			budget = budget,
			errorHandler = onError
		)
	}
	val entries = viewModel.entriesViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()
	Scaffold(
		topBar = {
			Column {
				BudgetMonthSelector(
					budgetReference = budget.toReference(),
					onDateClick = {
						calendarOpen = !calendarOpen
					},
					isExpanded = calendarOpen,
					onPrev = onPreviousBudget,
					onNext = onNextBudget,
					onCreate = { reference ->
						calendarOpen = false
						onCreateBudget(reference)
					},
				)
				AnimatedVisibility(
					visible = calendarOpen,
					enter = expandVertically(
						animationSpec = tween(300, easing = FastOutSlowInEasing),
						expandFrom = Alignment.Top
					),
					exit = shrinkVertically(
						animationSpec = tween(300, easing = FastOutSlowInEasing),
						shrinkTowards = Alignment.Top
					),
				) {
					Column {
						Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
						BudgetCalendarSelector(
							spaceId = space.id,
							currentBudget = budget.toReference(),
							onMonthSelected = { reference ->
								onMonthSelect(reference)
								calendarOpen = false
							},
							onError = onError,
						)
					}
				}
			}
		},
		floatingActionButton = {
			AddEntryButtonWithForm(
				space = space,
				currentBudget = budget.toReference(),
				loadingState = loadingState.value,
				onCreate = { budgetReference, type, label, amount, description ->
					viewModel.acceptIntent(
						EntryIntent.CreateEntry(
							budgetReference = budgetReference,
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
						loadingState = loadingState.value,
						onError = onError,
						onCreateEntry = { budgetReference, type, label, amount, description ->
							viewModel.acceptIntent(
								EntryIntent.CreateEntry(
									budgetReference = budgetReference,
									type = type,
									label = label,
									amount = amount,
									description = description,
								)
							)
						}
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