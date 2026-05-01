package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent
import kotlin.time.Clock

@Composable
fun MobileBudgetScreen(
	spaceId: String,
	onError: (e: Throwable) -> Unit
) {
	var calendarOpen by remember { mutableStateOf(false) }
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
					onDateClick = {
						calendarOpen = !calendarOpen
					},
					isExpanded = calendarOpen,
					onPrev = budgetView.previousBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToPrevious)} },
					onNext = budgetView.nextBudget?.let { { viewModel.acceptIntent(BudgetIntent.NavigateToNext)} },
					onCreate = { reference -> viewModel.acceptIntent(BudgetIntent.CreateBudget(reference)) }
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
							spaceId = spaceId,
							currentBudget = budgetView.currentBudget.toReference(),
							onMonthSelected = { reference ->
								viewModel.acceptIntent(BudgetIntent.NavigateTo(reference))
								calendarOpen = false
							},
							onError = onError,
						)
					}
				}
				BudgetComponent(
					refreshKey = Clock.System.now().toEpochMilliseconds(),
					spaceId = spaceId,
					budget = budgetView.currentBudget,
				)
			}
		}
	}
}