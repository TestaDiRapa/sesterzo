package org.testadirapa.sesterzo.screens.main.desktop.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.budget.BudgetSelector
import org.testadirapa.sesterzo.components.mobile.entries.AddEntryForm
import org.testadirapa.sesterzo.components.template.SourceUpdateForm
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.desktop.entries.DesktopEntryScreen
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.monthName
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.EntriesViewModel
import org.testadirapa.sesterzo.viewmodel.intents.EntryIntent
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_entry_form_type_add_button_desktop
import sesterzo.composeapp.generated.resources.main_page_template_form_subtitle
import sesterzo.composeapp.generated.resources.main_page_template_form_warning
import sesterzo.composeapp.generated.resources.plus
import sesterzo.composeapp.generated.resources.template_page_expenses
import sesterzo.composeapp.generated.resources.template_page_savings
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun DesktopNavBarDivider(
	space: Space,
	page: Page,
	budget: BudgetViewModel.BudgetWithTemplates,
	budgetLoadingState: Boolean,
	onPreviousBudget: (() -> Unit)?,
	onNextBudget: (() -> Unit)?,
	onMonthSelect: (reference: BudgetReference) -> Unit,
	onCreateBudget: (reference: BudgetReference) -> Unit,
	onBudgetUpdate: (budget: DecryptedBudget, newAmounts: Map<String, Amount>, type: Entry.EntryType) -> Unit,
	onError: (e: Throwable) -> Unit,
) {
	val viewModel = viewModel(key = "${space.id}-${budget.budget.id}-${budget.budget.version}") {
		EntriesViewModel(
			spaceId = space.id,
			budget = budget.budget,
			errorHandler = onError
		)
	}
	var addEntryFormDetails by remember { mutableStateOf<Pair<Entry.EntryType?, String?>?>(null) }
	var typeOfTemplateToUpdate by remember { mutableStateOf<Entry.EntryType?>(null) }
	val entries = viewModel.entriesViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		BudgetHeader(
			space = space,
			budget = budget.budget,
			onPreviousBudget = onPreviousBudget,
			onNextBudget = onNextBudget,
			onMonthSelect = onMonthSelect,
			onCreateBudget = onCreateBudget,
			onCreateEntry = {
				addEntryFormDetails = Pair(null, null)
			},
			onError = onError
		)
		HorizontalDivider(color = colorScheme.outline)
		when (page) {
			Page.Budget -> {
				DesktopBudgetScreen(
					budget = budget,
					entries = entries.value,
					onOpenCreateEntryForm = { type, label ->
						addEntryFormDetails = type to label
					},
					onEditMonthTemplate = { type ->
						typeOfTemplateToUpdate = type
					}
				)
			}
			Page.Entries -> {
				DesktopEntryScreen(
					entries = entries.value,
					onDeleteEntry = { entryId ->
						viewModel.acceptIntent(EntryIntent.DeleteEntry(entryId))
					}
				)
			}
			else -> {}
		}
	}

	addEntryFormDetails?.also { (entryType, label) ->
		Dialog(onDismissRequest = { addEntryFormDetails = null }) {
			Surface(
				shape = RoundedCornerShape(16.dp),
				color = colorScheme.surface,
				modifier = Modifier.width(480.dp)
			) {
				AddEntryForm(
					space = space,
					currentBudgetReference = budget.budget.toReference(),
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
						addEntryFormDetails = null
					},
					loadingState = loadingState.value,
					entryType = entryType ?: Entry.EntryType.Expense,
					label = label
				)
			}
		}
	}

	typeOfTemplateToUpdate?.also { type ->
		Dialog(onDismissRequest = { typeOfTemplateToUpdate = null }) {
			Surface(
				shape = RoundedCornerShape(16.dp),
				color = colorScheme.surface,
				modifier = Modifier.width(480.dp)
			) {
				SourceUpdateForm(
					title = when (type) {
						Entry.EntryType.Expense -> stringResource(Res.string.template_page_expenses)
						Entry.EntryType.Saving -> stringResource(Res.string.template_page_savings)
						Entry.EntryType.Income -> throw UnsupportedOperationException("Cannot edit income template")
					},
					type = stringResource(Res.string.main_page_template_form_subtitle),
					sources = when (type) {
						Entry.EntryType.Expense  -> budget.expensesTemplate.elements + budget.budget.fixedExpenses
						Entry.EntryType.Saving -> budget.savingsTemplate.elements + budget.budget.savings
						Entry.EntryType.Income -> throw UnsupportedOperationException("Cannot edit income template")
					},
					entity = when (type) {
						Entry.EntryType.Expense -> Triple(budget.budget, budget.expensesTemplate, type)
						Entry.EntryType.Saving -> Triple(budget.budget, budget.savingsTemplate, type)
						Entry.EntryType.Income -> throw UnsupportedOperationException("Cannot edit income template")
					},
					loadingState = budgetLoadingState,
					onSourceUpdate = { (budget, template, type), updatedAmounts, _ ->
						val amounts = updatedAmounts.filter { (k, v) ->
							template.elements[k] != v
						}
						onBudgetUpdate(budget, amounts, type)
						typeOfTemplateToUpdate = null
					},
					showUpdateCurrentBudgetSwitch = false,
					warningText = stringResource(Res.string.main_page_template_form_warning)
				)
			}
		}
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
	onCreateEntry: () -> Unit,
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
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp),
		) {
			BudgetSelector(
				space = space,
				budget = budget,
				onPreviousBudget = onPreviousBudget,
				onNextBudget = onNextBudget,
				onMonthSelect = onMonthSelect,
				onCreateBudget = onCreateBudget,
				onError = onError,
				modifier = Modifier.width(360.dp),
				useDialog = true
			)
			Button(
				onClick = onCreateEntry,
				shape = RoundedCornerShape(12.dp),
				modifier = Modifier.height(60.dp),
				colors = ButtonColors(
					containerColor = colorScheme.primary,
					contentColor = colorScheme.onPrimary,
					disabledContainerColor = colorScheme.surfaceContainerHigh,
					disabledContentColor = colorScheme.onTertiary,
				)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
				) {
					Icon(
						modifier = Modifier.size(30.dp),
						tint = colorScheme.onPrimary,
						painter = painterResource(Res.drawable.plus),
						contentDescription = null,
					)
					Text(
						text = stringResource(Res.string.add_entry_form_type_add_button_desktop),
						fontSize = 17.sp,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.onPrimary,
					)
				}
			}
		}
	}
}