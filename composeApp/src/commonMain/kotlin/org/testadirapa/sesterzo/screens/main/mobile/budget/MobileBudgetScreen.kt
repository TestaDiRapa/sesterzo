package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.mobile.budget.BudgetCalendarSelector
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.components.mobile.budget.BudgetMonthSelector
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent
import kotlin.time.Clock

@Composable
fun MobileBudgetScreen(
	spaceId: String,
	onError: (e: Throwable) -> Unit
) {
	val viewModel = viewModel(key = "budget") {
		BudgetViewModel(spaceId = spaceId, errorHandler = onError)
	}
	val budgetView = viewModel.budgetViewState.collectAsState()
	budgetView.value?.let { budgetView ->
		Scaffold { innerPadding ->
			Column(
				modifier = Modifier
					.padding(paddingValues = innerPadding)
					.padding(horizontal = 16.dp)
					.fillMaxSize()
			) {
				BudgetMonthSelector(
					budgetReference = budgetView.currentBudget.toReference(),
					onDateClick = {},
					onPrev = budgetView.previousBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToPrevious)} },
					onNext = budgetView.nextBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToNext)} },
					onCreate = { reference -> viewModel.acceptIntent(BudgetIntent.CreateBudget(reference)) }
				)
				BudgetCalendarSelector(
					spaceId = spaceId,
					currentBudget = budgetView.currentBudget.toReference(),
					onError = onError,
				)
				BudgetComponent(
					refreshKey = Clock.System.now().toEpochMilliseconds(),
					spaceId = spaceId,
					budget = budgetView.currentBudget,
				)
			}
		}
	}
}