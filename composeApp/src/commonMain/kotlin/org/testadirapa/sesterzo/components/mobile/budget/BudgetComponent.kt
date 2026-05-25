package org.testadirapa.sesterzo.components.mobile.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.mobile.entries.AddEntryForm
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.toReference
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.main_page_mode_expenses
import sesterzo.composeapp.generated.resources.main_page_mode_savings

private enum class DisplayMode(val entryType: Entry.EntryType) {
	Expenses(Entry.EntryType.Expense),
	Savings(Entry.EntryType.Saving),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetComponent(
	space: Space,
	budget: DecryptedBudget,
	scaffoldPadding: PaddingValues,
	entries: List<DecryptedEntry>,
	loadingState: Boolean,
	onCreateEntry: (budgetReference: BudgetReference, type: Entry.EntryType, label: String, amount: Amount, description: String?) -> Unit,
	onError: (error: Throwable) -> Unit,
) {
	var addEntryFormInfo by remember { mutableStateOf<Pair<String, Entry.EntryType>?>(null) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var displayMode by remember { mutableStateOf(DisplayMode.Expenses) }
	var expensesTemplate by remember { mutableStateOf<DecryptedBudgetElement?>(null) }
	var savingsTemplate by remember { mutableStateOf<DecryptedBudgetElement?>(null) }
	LaunchedEffect(budget.expensesReference) {
		runCatching {
			expensesTemplate = AppCtx.api.budgetElement.getBudgetElement(
				spaceId = budget.spaceId,
				budgetElementReference = budget.expensesReference
			)
			savingsTemplate = AppCtx.api.budgetElement.getBudgetElement(
				spaceId = budget.spaceId,
				budgetElementReference = budget.savingsReference
			)
		}.onFailure(onError)
	}
	Column(
		modifier = Modifier.padding(scaffoldPadding)
	) {
		ModeSelector(
			mode = displayMode,
			entries = entries,
			onModeSelected = { mode ->
				displayMode = mode
			}
		)
		Spacer(modifier = Modifier.height(16.dp))
		BudgetSummaryCard(
			title = when(displayMode) {
				DisplayMode.Expenses -> stringResource(Res.string.main_page_mode_expenses)
				DisplayMode.Savings -> stringResource(Res.string.main_page_mode_savings)
			},
			scheduled = when(displayMode) {
				DisplayMode.Expenses -> expensesTemplate?.elements.orEmpty() + budget.fixedExpenses
				DisplayMode.Savings -> savingsTemplate?.elements.orEmpty() + budget.savings
			},
			entries = entries.filter { !it.deleted && it.type == displayMode.entryType },
			overLimitTextColor = when(displayMode) {
				DisplayMode.Expenses -> LocalFinanceColors.current.spent
				DisplayMode.Savings -> LocalFinanceColors.current.saved
			},
			onRowClick = { label ->
				addEntryFormInfo = label to displayMode.entryType
			}
		)
	}
	if (addEntryFormInfo != null) {
		ModalBottomSheet(
			onDismissRequest = { addEntryFormInfo = null },
			sheetState = sheetState,
			containerColor = colorScheme.surface,
		) {
			AddEntryForm(
				space = space,
				currentBudgetReference = budget.toReference(),
				onCreate = { budgetReference, type, label, amount, description ->
					onCreateEntry(budgetReference, type, label, amount, description)
					addEntryFormInfo = null
				},
				loadingState = loadingState,
				entryType = addEntryFormInfo?.second ?: Entry.EntryType.Expense,
				label = addEntryFormInfo?.first,
			)
		}
	}

}

@Composable
private fun ModeSelector(
	mode: DisplayMode,
	entries: List<DecryptedEntry>,
	onModeSelected: (DisplayMode) -> Unit,
) {
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Row(
			modifier = Modifier.fillMaxWidth().padding(6.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			ModeSelectorCard(
				text = stringResource(Res.string.main_page_mode_expenses),
				amount = entries.filter { !it.deleted && it.type == Entry.EntryType.Expense }.sumOf { it.amount },
				isSelected = mode == DisplayMode.Expenses,
				modifier = Modifier
					.weight(1f)
					.clickable(onClick = { onModeSelected(DisplayMode.Expenses) }),
			)
			ModeSelectorCard(
				text = stringResource(Res.string.main_page_mode_savings),
				amount = entries.filter { !it.deleted && it.type == Entry.EntryType.Saving }.sumOf { it.amount },
				isSelected = mode == DisplayMode.Savings,
				modifier = Modifier
					.weight(1f)
					.clickable(onClick = { onModeSelected(DisplayMode.Savings) }),
			)
		}
	}
}

@Composable
private fun ModeSelectorCard(
	text: String,
	amount: Amount,
	isSelected: Boolean,
	modifier: Modifier,
) {
	Card(
		border = BorderStroke(
			width = if (isSelected) 1.dp else 0.dp,
			color = if (isSelected) colorScheme.outline else colorScheme.surface
		),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) colorScheme.surfaceVariant else colorScheme.surface
		),
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 12.dp, horizontal = 12.dp)
		) {
			Text(
				text = text,
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.Bold,
				color = if (isSelected) colorScheme.onSurface else colorScheme.onSurfaceVariant,
				modifier = Modifier.weight(1f)
			)
			Text(
				text = AppCtx.currency.writer(amount),
				style = MaterialTheme.typography.labelMedium,
				color = colorScheme.onSurfaceVariant,
				textAlign = TextAlign.End,
				modifier = Modifier.weight(1f)
			)
		}
	}
}