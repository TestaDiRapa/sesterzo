package org.testadirapa.sesterzo.components.mobile.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry

@Composable
fun BudgetSummaryCard(
	title: String,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>
) {
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Column {
			CardHeader(
				title = title,
				total = scheduled.values.sum(),
				actual = entries.sumOf { it.amount }
			)
			HorizontalDivider(modifier = Modifier.fillMaxWidth())
		}
	}
}

@Composable
private fun CardHeader(
	title: String,
	total: Amount,
	actual: Amount,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth()
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = title,
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
			Text(
				text = " · ${AppCtx.currency.symbol} ",
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
			Text(
				text = AppCtx.currency.formWriter(actual),
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
			Text(
				text = " / ${AppCtx.currency.formWriter(total)}",
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}