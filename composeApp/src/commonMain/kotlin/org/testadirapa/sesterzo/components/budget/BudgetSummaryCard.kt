package org.testadirapa.sesterzo.components.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.EditButton
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.styles.typography.amountTextStyleSmall

@Composable
fun BudgetSummaryCard(
	title: String?,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>,
	overLimitTextColor: Color,
	onRowClick: (label: String) -> Unit,
	onEdit: () -> Unit,
) {
	val cardRowsValues = getRowValues(scheduled = scheduled, entries = entries)
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Column {
			if (title != null) {
				CardHeader(
					title = title,
					total = scheduled.values.sum(),
					actual = entries.sumOf { it.amount },
					overLimitTextColor = overLimitTextColor,
					onEdit = onEdit
				)
				HorizontalDivider(modifier = Modifier.fillMaxWidth())
			}
			cardRowsValues.forEachIndexed { idx, (label, amount, threshold) ->
				key(label) {
					SummaryRow(
						label = label,
						amount = amount,
						threshold = threshold,
						overLimitTextColor = overLimitTextColor,
						onClick = { onRowClick(label) },
						idx = idx
					)
				}
				if (idx != cardRowsValues.lastIndex) {
					HorizontalDivider(modifier = Modifier.fillMaxWidth())
				}
			}
		}
	}
}

@Composable
private fun CardHeader(
	title: String,
	total: Amount,
	actual: Amount,
	overLimitTextColor: Color,
	onEdit: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth().padding(12.dp)
	) {
		Column{
			Text(
				text = title,
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.height(4.dp))
			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = AppCtx.currency.writer(actual),
					color = if (actual <= total) {
						colorScheme.onSurface
					} else {
						overLimitTextColor
					},
					style = amountTextStyleSmall()
				)
				Text(
					text = " / ",
					color = colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text =  AppCtx.currency.writer(total),
					color = colorScheme.onSurfaceVariant,
					style = amountTextStyleSmall()
				)
			}
		}
		EditButton(
			onEdit = onEdit,
		)
	}
}