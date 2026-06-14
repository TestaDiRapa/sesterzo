package org.testadirapa.sesterzo.screens.main.desktop.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.budget.BudgetSelector
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.monthName
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.EntriesViewModel

@Composable
fun DesktopBudgetScreen(
	space: Space,
	page: Page,
	budget: DecryptedBudget,
	budgetLoadingState: Boolean,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onBudgetUpdate: (budget: DecryptedBudget, newAmounts: Map<String, Amount>, type: Entry.EntryType) -> Unit,
	onError: (e: Throwable) -> Unit,
) {
	val viewModel = viewModel(key = "${space.id}-${budget.id}-${budget.version}") {
		EntriesViewModel(
			spaceId = space.id,
			budget = budget,
			errorHandler = onError
		)
	}
	var calendarOpen by remember { mutableStateOf(false) }
	val entries = viewModel.entriesViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		BudgetHeader(
			space = space,
			budget = budget,
			onPreviousBudget = onPreviousBudget,
			onNextBudget = onNextBudget,
			onMonthSelect = onMonthSelect,
			onCreateBudget = onCreateBudget,
			onError = onError
		)
		HorizontalDivider(color = colorScheme.outline)
	}
}

@Composable
fun BudgetHeader(
	space: Space,
	budget: DecryptedBudget,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onError: (e: Throwable) -> Unit
) {
	Row(
		modifier = Modifier.padding(16.dp).fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Column {
			Text(
				text = space.name,
				fontSize = 17.sp,
				fontWeight = FontWeight.Bold,
				color = colorScheme.onSurface,
			)
			Spacer(Modifier.height(4.dp))
			Text(
				text = "${monthName(budget.toReference().month, abbreviated = false)} ${budget.year}",
				style = MaterialTheme.typography.labelLarge,
				color = colorScheme.onSurfaceVariant,
			)
		}
		BudgetSelector(
			space = space,
			budget = budget,
			onPreviousBudget = onPreviousBudget,
			onNextBudget = onNextBudget,
			onMonthSelect = onMonthSelect,
			onCreateBudget = onCreateBudget,
			onError = onError,
			modifier = Modifier.width(360.dp),
			floatOverContent = true
		)
	}
}