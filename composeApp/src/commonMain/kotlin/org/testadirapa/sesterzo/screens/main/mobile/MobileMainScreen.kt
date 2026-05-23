package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.mobile.main.BottomMenu
import org.testadirapa.sesterzo.components.mobile.main.HeaderBar
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen
import org.testadirapa.sesterzo.screens.main.mobile.template.MobileTemplateScreen
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent

enum class Page { Budget, Entries, Template }

@Composable
fun MobileMainScreen(
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	var currentPage by remember { mutableStateOf(Page.Budget) }
	var space by remember { mutableStateOf(initialSpace) }
	val viewModel = viewModel(key = "${space.id}-budget") {
		BudgetViewModel(spaceId = space.id, errorHandler = onError)
	}
	val budgetView = viewModel.budgetViewState.collectAsState()
	Scaffold(
		topBar = {
			HeaderBar(
				space = space,
				onCreateSpace = onCreateSpace,
				onSwitchSpace = { space = it },
				onError = onError,
			)
		},
		bottomBar = {
			BottomMenu(
				currentPage = currentPage,
				onPageChange = { currentPage = it },
			)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			budgetView.value?.let { budgets ->
				when(currentPage) {
					Page.Budget, Page.Entries -> {
						MobileBudgetScreen(
							space = space,
							page = currentPage,
							budget = budgets.currentBudget,
							onPreviousBudget = budgets.previousBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToPrevious) } },
							onNextBudget = budgets.nextBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToNext) } },
							onMonthSelect = { reference ->
								viewModel.acceptIntent(BudgetIntent.NavigateTo(reference))
							},
							onCreateBudget = { reference ->
								viewModel.acceptIntent(BudgetIntent.CreateBudget(reference))
							},
							onError = onError,
						)
					}
					Page.Template -> {
						MobileTemplateScreen(
							space = space,
							onUpdateBudgetsTemplate = { type, reference, updateCurrentBudget ->
								viewModel.acceptIntent(
									BudgetIntent.UpdateCurrentBudgetTemplate(type, reference, updateCurrentBudget)
								)
							},
							onError = onError,
						)
					}
				}
			}
		}
	}
}