package org.testadirapa.sesterzo.screens.main.desktop.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.budget.BudgetStats
import org.testadirapa.sesterzo.components.desktop.budget.BudgetSectionSelector
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.utils.daysToEndOfValidity
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.arrow_forward
import sesterzo.composeapp.generated.resources.banknotes
import sesterzo.composeapp.generated.resources.template_page_expenses
import sesterzo.composeapp.generated.resources.template_page_income_sources
import sesterzo.composeapp.generated.resources.template_page_savings
import kotlin.collections.plus

private data class MenuOption(
	val label: String,
	val amount: Amount,
	val painter: Painter,
	val color: Color,
	val type: Entry.EntryType
)

@Composable
fun DesktopBudgetScreen(
	budget: BudgetViewModel.BudgetWithTemplates,
	entries: List<DecryptedEntry>,
	onOpenCreateEntryForm: (Entry.EntryType, String) -> Unit,
	onEditMonthTemplate: (Entry.EntryType) -> Unit,
) {
	var displayMode by remember { mutableStateOf(Entry.EntryType.Expense) }
	Row(
		modifier = Modifier.fillMaxSize(),
	) {
		Column(
			modifier = Modifier.weight(1f),
		) {
			val budgetReference = budget.budget.toReference()
			val incomeTotal = entries.filter { !it.deleted && it.type == Entry.EntryType.Income }.sumOf { it.amount }
			val spentTotal = entries.filter { !it.deleted && it.type == Entry.EntryType.Expense }.sumOf { it.amount }
			val savedTotal = entries.filter { !it.deleted && it.type == Entry.EntryType.Saving }.sumOf { it.amount }
			BudgetStats(
				month = budgetReference.month,
				daysLeft = budgetReference.daysToEndOfValidity(),
				incomeTotal = incomeTotal,
				spentTotal = spentTotal,
				savedTotal = savedTotal,
				modifier = Modifier.padding(all = 16.dp)
			)
			HorizontalDivider()
			Column(
				modifier = Modifier.padding(all = 16.dp),
			) {
				val options = listOf(
					MenuOption(
						label = stringResource(Res.string.template_page_income_sources),
						amount = incomeTotal,
						painter = painterResource(Res.drawable.arrow_forward),
						color = colorScheme.primary,
						type = Entry.EntryType.Income
					),
					MenuOption(
						label = stringResource(Res.string.template_page_expenses),
						amount = spentTotal,
						painter = painterResource(Res.drawable.arrow_back),
						color = LocalFinanceColors.current.spent,
						type = Entry.EntryType.Expense
					),
					MenuOption(
						label = stringResource(Res.string.template_page_savings),
						amount = savedTotal,
						painter = painterResource(Res.drawable.banknotes),
						color = LocalFinanceColors.current.saved,
						type = Entry.EntryType.Saving
					),
				)
				options.forEach { options ->
					Spacer(modifier = Modifier.height(16.dp))
					BudgetSectionSelector(
						label = options.label,
						isSelected = displayMode == options.type,
						totalAmount = options.amount,
						color = options.color,
						painter = options.painter,
						onClick = {
							displayMode = options.type
						}
					)
				}
			}
		}
		VerticalDivider(color = colorScheme.outline)
		Column(
			modifier = Modifier.weight(2f).verticalScroll(rememberScrollState())
		) {
			DesktopBudgetDetailsScreen(
				label = when (displayMode) {
					Entry.EntryType.Expense -> stringResource(Res.string.template_page_expenses)
					Entry.EntryType.Saving -> stringResource(Res.string.template_page_savings)
					Entry.EntryType.Income -> stringResource(Res.string.template_page_income_sources)
				},
				color = when (displayMode) {
					Entry.EntryType.Expense -> LocalFinanceColors.current.spent
					Entry.EntryType.Saving -> LocalFinanceColors.current.saved
					Entry.EntryType.Income -> colorScheme.primary
				},
				type = displayMode,
				scheduled = when(displayMode) {
					Entry.EntryType.Expense -> budget.expensesTemplate.elements + budget.budget.fixedExpenses
					Entry.EntryType.Saving -> budget.savingsTemplate.elements + budget.budget.savings
					Entry.EntryType.Income -> budget.incomeTemplate.elements
				},
				entries = entries.filter { !it.deleted && it.type == displayMode },
				onOpenCreateEntryForm = onOpenCreateEntryForm,
				onEditMonthTemplate = onEditMonthTemplate,
			)
		}
	}
}