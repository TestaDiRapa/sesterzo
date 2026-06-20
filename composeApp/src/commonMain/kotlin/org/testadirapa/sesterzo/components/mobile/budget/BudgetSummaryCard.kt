package org.testadirapa.sesterzo.components.mobile.budget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.EditButton
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.styles.typography.amountTextStyleMedium
import org.testadirapa.sesterzo.styles.typography.amountTextStyleSmall
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BudgetSummaryCard(
	title: String,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>,
	overLimitTextColor: Color,
	onRowClick: (label: String) -> Unit,
	onEdit: () -> Unit,
) {
	val entriesByLabel = entries.groupBy { it.label }.mapValues { it.value.sumOf { v -> v.amount } }.toMutableMap()
	val cardRowsValues = scheduled.map { (label, amount) ->
		val actual = entriesByLabel.remove(label)
		Triple(label, actual ?: 0, amount)
	} + entriesByLabel.map { (label, amount) ->
		Triple(label, amount, 0L)
	}
	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Column {
			CardHeader(
				title = title,
				total = scheduled.values.sum(),
				actual = entries.sumOf { it.amount },
				overLimitTextColor = overLimitTextColor,
				onEdit = onEdit
			)
			HorizontalDivider(modifier = Modifier.fillMaxWidth())
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
private fun SummaryRow(
	label: String,
	amount: Amount,
	threshold: Amount,
	overLimitTextColor: Color,
	onClick: () -> Unit,
	idx: Int = 0
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable(onClick = onClick)
			.padding(12.dp)
	) {
		Column {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = label,
					color = colorScheme.onSurface,
					style = MaterialTheme.typography.bodyLarge,
					fontWeight = FontWeight.SemiBold,
				)
				Text(
					text = AppCtx.currency.writer(amount),
					color = colorScheme.onSurface,
					style = amountTextStyleMedium()
				)
			}
			Spacer(modifier = Modifier.height(8.dp))
			AnimatedProgressBar(
				value = (amount.toFloat() / threshold.toFloat()).coerceAtMost(1.0f),
				overLimitColor = overLimitTextColor,
				delayMillis = 150 * idx
			)
		}
	}
}

@Composable
fun AnimatedProgressBar(
	value: Float,
	overLimitColor: Color,
	delayMillis: Int = 0
) {
	var progress by remember { mutableFloatStateOf(0f) }
	val animatedProgress by animateFloatAsState(
		targetValue = progress,
		animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
		label = "progress"
	)

	LaunchedEffect(value) {
		delay(delayMillis.milliseconds)
		progress = value
	}

	LinearProgressIndicator(
		progress = { animatedProgress },
		color = if (value >= 1.0) overLimitColor else ProgressIndicatorDefaults.linearColor,
		modifier = Modifier.fillMaxWidth()
	)
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