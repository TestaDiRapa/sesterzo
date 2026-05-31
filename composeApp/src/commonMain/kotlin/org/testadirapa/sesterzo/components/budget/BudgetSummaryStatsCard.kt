package org.testadirapa.sesterzo.components.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import org.testadirapa.sesterzo.utils.monthName
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_entry_form_type_income
import sesterzo.composeapp.generated.resources.budget_summary_stats_card_days_left
import sesterzo.composeapp.generated.resources.budget_summary_stats_card_saved
import sesterzo.composeapp.generated.resources.budget_summary_stats_card_spent

@Composable
fun BudgetSummaryStatsCard(
	month: Month,
	daysLeft: Int,
	incomeTotal: Amount,
	spentTotal: Amount,
	savedTotal: Amount,
) {
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	){
		Column(modifier = Modifier.padding(
			start = 16.dp, end = 16.dp,
			top   = 14.dp, bottom = 12.dp,
		)) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				Text(
					text = monthName(month, abbreviated = false).uppercase(),
					style = MaterialTheme.typography.titleMedium,
					color = colorScheme.onSurfaceVariant,
				)
				Text(
					text = "$daysLeft ${stringResource(Res.string.budget_summary_stats_card_days_left)}",
					style = MaterialTheme.typography.titleMedium,
					color = colorScheme.onSurfaceVariant,
				)
			}

			Spacer(Modifier.height(10.dp))

			Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
				StatColumn(label = stringResource(Res.string.add_entry_form_type_income), amount = incomeTotal, color = colorScheme.onSurface)
				StatColumn(label = stringResource(Res.string.budget_summary_stats_card_spent),  amount = spentTotal,  color = LocalFinanceColors.current.spent)
				StatColumn(label = stringResource(Res.string.budget_summary_stats_card_saved),  amount = savedTotal,  color = LocalFinanceColors.current.saved)
			}

			Spacer(Modifier.height(14.dp))

			StackedProgressBar(
				spent = spentTotal,
				saved = savedTotal,
				total = incomeTotal,
			)
		}
	}
}

@Composable
private fun StatColumn(
	label: String,
	amount: Amount,
	color: Color
) {
	Column {
		Text(
			text = AppCtx.currency.writer(amount),
			color = color,
			style = amountTextStyleLarge(),
			fontWeight = FontWeight.Bold,
		)
		Text(
			text = label,
			color = colorScheme.onTertiaryContainer,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.bodyMedium,
		)
	}
}

@Composable
private fun StackedProgressBar(
	spent: Amount,
	saved: Amount,
	total: Amount,
) {
	val denominator = (spent + saved).coerceAtLeast(total).toFloat()
	val spentFrac = (spent.toFloat() / denominator).coerceIn(0f, 1f)
	val savedFrac = (saved.toFloat() / denominator).coerceIn(0f, 1f - spentFrac)

	val spentColor = LocalFinanceColors.current.spent
	val savedColor = LocalFinanceColors.current.saved
	Canvas(
		modifier = Modifier
			.fillMaxWidth()
			.height(6.dp)
			.clip(RoundedCornerShape(999.dp))
			.background(LocalFinanceColors.current.free),
	) {
		val w = size.width
		val h = size.height
		val spentW = w * spentFrac
		val savedW = w * savedFrac
		drawRect(spentColor, topLeft = Offset(0f, 0f), size = Size(spentW, h))
		drawRect(savedColor, topLeft = Offset(spentW, 0f), size = Size(savedW, h))
	}
}