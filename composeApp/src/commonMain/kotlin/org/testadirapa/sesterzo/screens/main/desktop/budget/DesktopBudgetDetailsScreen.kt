package org.testadirapa.sesterzo.screens.main.desktop.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.budget.SummaryRow
import org.testadirapa.sesterzo.components.budget.getRowValues
import org.testadirapa.sesterzo.components.input.EditButton
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleVeryLarge
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.budget_page_desktop_actual_label
import sesterzo.composeapp.generated.resources.budget_page_desktop_expected_label

@Composable
fun DesktopBudgetDetailsScreen(
	label: String,
	color: Color,
	type: Entry.EntryType,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>,
	onOpenCreateEntryForm: (Entry.EntryType, String) -> Unit,
	onEditMonthTemplate: (Entry.EntryType) -> Unit,
) {
	val cardRowsValues = getRowValues(scheduled = scheduled, entries = entries)
	val overLimitTextColor = when(type) {
		Entry.EntryType.Expense -> LocalFinanceColors.current.spent
		Entry.EntryType.Income -> colorScheme.primary
		Entry.EntryType.Saving -> LocalFinanceColors.current.saved
	}
	Column(
		modifier = Modifier.padding(all = 32.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			DetailsHeader(label = label, color = color)
			if (type != Entry.EntryType.Income) {
				EditButton(onEdit = { onEditMonthTemplate(type) })
			}
		}
		Spacer(modifier = Modifier.height(8.dp))
		SummaryCards(
			type = type,
			scheduled = scheduled,
			entries = entries,
		)
		Spacer(modifier = Modifier.height(16.dp))
		cardRowsValues.forEachIndexed { idx, (label, amount, threshold) ->
			key(label) {
				Card(
					border = BorderStroke(width = 1.dp, color = colorScheme.outline),
					colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
					modifier = Modifier.fillMaxWidth(),
				) {
					SummaryRow(
						label = label,
						amount = amount,
						threshold = threshold,
						overLimitTextColor = overLimitTextColor,
						onClick = {
							onOpenCreateEntryForm(type, label)
						},
						idx = idx,
					)
				}
				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}

@Composable
private fun SummaryCards(
	type: Entry.EntryType,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>
) {
	val totalScheduled = scheduled.values.sum()
	val actualTotal = entries.sumOf { it.amount }
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		SummaryCard(
			label = stringResource(Res.string.budget_page_desktop_expected_label),
			amount = totalScheduled,
			color = colorScheme.onBackground,
			modifier = Modifier.weight(1f),
		)
		SummaryCard(
			label = stringResource(Res.string.budget_page_desktop_actual_label),
			amount = actualTotal,
			color = when {
				(type == Entry.EntryType.Income || type == Entry.EntryType.Saving) &&
					totalScheduled > actualTotal -> colorScheme.onBackground
				type == Entry.EntryType.Income || type == Entry.EntryType.Saving -> LocalFinanceColors.current.saved
				totalScheduled <= actualTotal -> LocalFinanceColors.current.spent
				else -> LocalFinanceColors.current.saved
			},
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun SummaryCard(
	label: String,
	amount: Amount,
	color: Color,
	modifier: Modifier = Modifier,
) {
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
		modifier = modifier.padding(all = 16.dp),
	) {
		Column(
			modifier = Modifier.fillMaxWidth().height(96.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = label,
				color = colorScheme.onTertiaryContainer,
				textAlign = TextAlign.Start,
				style = MaterialTheme.typography.bodyMedium,
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = AppCtx.currency.writer(amount),
				color = color,
				style = amountTextStyleVeryLarge(),
				fontWeight = FontWeight.Bold,
			)
		}
	}
}

@Composable
private fun DetailsHeader(
	label: String,
	color: Color,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.width(7.dp)
				.height(7.dp)
				.background(color, RoundedCornerShape(1.dp))
		)
		Spacer(modifier = Modifier.width(8.dp))
		Text(
			text = label,
			color = colorScheme.onBackground,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)
	}
}