package org.testadirapa.sesterzo.components.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.styles.typography.amountTextStyleMedium
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_down
import sesterzo.composeapp.generated.resources.budget_summary_stats_card_hide_income_details
import sesterzo.composeapp.generated.resources.budget_summary_stats_card_show_income_details

@Composable
fun BudgetSummaryStatsCard(
	month: Month,
	daysLeft: Int,
	incomeTotal: Amount,
	spentTotal: Amount,
	savedTotal: Amount,
	incomeSources: Map<String, Amount>
) {
	var expanded by remember { mutableStateOf(false) }
	val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f)
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	){
		Column(
			modifier = Modifier.padding(
				start = 16.dp,
				end = 16.dp,
				top = 14.dp,
				bottom = 12.dp,
			)
		) {

			BudgetStats(
				month = month,
				daysLeft = daysLeft,
				incomeTotal = incomeTotal,
				spentTotal = spentTotal,
				savedTotal = savedTotal
			)

			Spacer(Modifier.height(14.dp))
			AnimatedVisibility(visible = expanded) {
				Column {
					incomeSources.entries.forEachIndexed { idx, (label, value) ->
						if (idx > 0) {
							Spacer(Modifier.height(14.dp))
						}
						IncomeRow(label, value)
						Spacer(Modifier.height(14.dp))
						if (idx != incomeSources.entries.size - 1) {
							HorizontalDivider(color = colorScheme.outline)
						}
					}
				}
			}

			HorizontalDivider(color = colorScheme.outline)
			Spacer(Modifier.height(8.dp))
			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.clickable { expanded = !expanded }
			) {
				Text(
					text = if (!expanded) {
						stringResource(Res.string.budget_summary_stats_card_show_income_details)
					} else {
						stringResource(Res.string.budget_summary_stats_card_hide_income_details)
					},
					color = colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyMedium,
				)
				Icon(
					modifier = Modifier
						.size(14.dp)
						.rotate(arrowRotation),
					painter = painterResource(Res.drawable.arrow_down),
					contentDescription = null,
					tint = colorScheme.onSurfaceVariant,
				)
			}
		}
	}
}

@Composable
private fun IncomeRow(
	label: String,
	value: Amount,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Spacer(Modifier.width(4.dp))
			Box(
				modifier = Modifier
					.width(5.dp)
					.height(30.dp)
					.background(colorScheme.primary, RoundedCornerShape(5.dp))
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = label,
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyLarge
			)
		}
		Text(
			text = AppCtx.currency.writer(value),
			color = colorScheme.onSurface,
			style = amountTextStyleMedium()
		)
	}
}