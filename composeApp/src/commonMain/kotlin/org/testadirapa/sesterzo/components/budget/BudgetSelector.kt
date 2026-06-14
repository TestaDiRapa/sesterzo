package org.testadirapa.sesterzo.components.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
	floatOverContent: Boolean = false,
) {
	var calendarOpen by remember { mutableStateOf(false) }
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
			modifier = Modifier.floatingOverlay(enabled = floatOverContent),
		) {
			Column {
				Spacer(modifier = Modifier.height(16.dp))
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
		}
	}
}

private fun Modifier.floatingOverlay(enabled: Boolean): Modifier =
	if (!enabled) {
		this
	} else {
		this
			.zIndex(1f)
			.layout { measurable, constraints ->
				val placeable = measurable.measure(constraints)
				// Report zero size so the floating content reserves no space in the
				// parent (neither vertically nor horizontally), then draw it on top.
				layout(0, 0) {
					placeable.place(0, 0)
				}
			}
	}
