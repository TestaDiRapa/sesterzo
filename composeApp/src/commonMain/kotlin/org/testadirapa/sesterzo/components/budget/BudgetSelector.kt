package org.testadirapa.sesterzo.components.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toReference

@Composable
fun BudgetSelector(
	space: Space,
	budget: DecryptedBudget,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onError: (error: Throwable) -> Unit,
	modifier: Modifier,
	useDialog: Boolean = false,
) {
	var calendarOpen by remember { mutableStateOf(false) }
	val calendar = @Composable {
		BudgetCalendarSelector(
			spaceId = space.id,
			currentBudget = budget.toReference(),
			onMonthSelected = { reference ->
				onMonthSelect(reference)
				calendarOpen = false
			},
			onError = onError,
			modifier = modifier,
		)
	}
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
			modifier = modifier,
		)
		if (useDialog) {
			// Desktop: render the calendar in a Dialog so it floats above every
			// sibling/parent in the layout instead of being clipped to this Column.
			if (calendarOpen) {
				Dialog(
					onDismissRequest = { calendarOpen = false },
					properties = DialogProperties(usePlatformDefaultWidth = false),
				) {
					Card(
						modifier = Modifier.padding(vertical = 16.dp),
						border = BorderStroke(width = 1.dp, color = colorScheme.outline),
						colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
					) {
						Column(modifier = Modifier.padding(16.dp)) {
							calendar()
						}
					}
				}
			}
		} else {
			// Mobile: expand the calendar inline below the month selector.
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
					Spacer(modifier = Modifier.height(16.dp))
					calendar()
				}
			}
		}
	}
}
