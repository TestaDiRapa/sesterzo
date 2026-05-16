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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.mobile.budget.BudgetCalendarSelector
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.components.mobile.budget.BudgetMonthSelector
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.SpaceViewModel

@Composable
fun MobileBudgetScreen(
	spaceId: String,
	budgetView: SpaceViewModel.BudgetView?,
	onNavigateToPrevious: () -> Unit,
	onNavigateToNext: () -> Unit,
	onCreate: (reference: BudgetReference) -> Unit,
	onSelect: (reference: BudgetReference) -> Unit,
	entries: List<DecryptedEntry>,
	onError: (e: Throwable) -> Unit,
) {
	var calendarOpen by remember { mutableStateOf(false) }
	budgetView?.let { budgetView ->
		Column {
				BudgetMonthSelector(
					budgetReference = budgetView.currentBudget.toReference(),
					onDateClick = {
						calendarOpen = !calendarOpen
					},
					isExpanded = calendarOpen,
					onPrev = budgetView.previousBudget?.let { onNavigateToPrevious },
					onNext = budgetView.nextBudget?.let { onNavigateToNext },
					onCreate = onCreate,
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
								onSelect(reference)
								calendarOpen = false
							},
							onError = onError,
						)
					}
				}
				BudgetComponent(
					entries = entries,
				)
		}
	}
}