package org.testadirapa.sesterzo.components.budget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.styles.typography.amountTextStyleMedium
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SummaryRow(
	label: String,
	amount: Amount,
	threshold: Amount,
	overLimitTextColor: Color,
	onClick: () -> Unit,
	idx: Int = 0,
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
			val fraction = if (threshold.toFloat() > 0f) {
				(amount.toFloat() / threshold.toFloat()).coerceIn(0f, 1.0f)
			} else {
				if (amount.toFloat() > 0f) 1.0f else 0f
			}
			AnimatedProgressBar(
				value = fraction,
				overLimitColor = overLimitTextColor,
				delayMillis = 150 * idx.coerceAtMost(9)
			)
		}
	}
}

@Composable
private fun AnimatedProgressBar(
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

fun getRowValues(
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>,
): List<Triple<String, Long, Amount>> {
	val entriesByLabel = entries.groupBy { it.label }.mapValues { it.value.sumOf { v -> v.amount } }.toMutableMap()
	return scheduled.map { (label, amount) ->
		val actual = entriesByLabel.remove(label)
		Triple(label, actual ?: 0, amount)
	} + entriesByLabel.map { (label, amount) ->
		Triple(label, amount, 0L)
	}
}