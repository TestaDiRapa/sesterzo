package org.testadirapa.sesterzo.components.template

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.template_page_expenses
import sesterzo.composeapp.generated.resources.template_page_free
import sesterzo.composeapp.generated.resources.template_page_income
import sesterzo.composeapp.generated.resources.template_page_month_abbreviated
import sesterzo.composeapp.generated.resources.template_page_savings

private data class BudgetCategory(
	val label: String,
	val amount: Amount,
	val color: Color,
)

@Composable
fun TemplateStatsCard(
	incomeSources: DecryptedBudgetElement,
	expenses: DecryptedBudgetElement,
	savings: DecryptedBudgetElement,
) {
	var visible by remember { mutableStateOf(false) }
	LaunchedEffect(Unit) { visible = true }

	val animProgress by animateFloatAsState(
		targetValue = if (visible) 1f else 0f,
		animationSpec = tween(durationMillis = 900),
		label = "donut_progress"
	)

	val totalExpenses = expenses.elements.values.sum()
	val totalSavings = savings.elements.values.sum()
	val totalIncome = incomeSources.elements.values.sum()
	val categories = listOf(
		BudgetCategory(
			label = stringResource(Res.string.template_page_expenses),
			amount = totalExpenses,
			color = LocalFinanceColors.current.spent,
		),
		BudgetCategory(
			label = stringResource(Res.string.template_page_savings),
			amount = totalSavings,
			color = LocalFinanceColors.current.saved,
		),
		BudgetCategory(
			label = stringResource(Res.string.template_page_free),
			amount = totalIncome - totalExpenses - totalSavings,
			color = LocalFinanceColors.current.free,
		)
	)

	Box(
		modifier = Modifier
			.background(colorScheme.surface, RoundedCornerShape(12.dp))
			.padding(horizontal = 24.dp, vertical = 20.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(28.dp)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size(160.dp)
			) {
				DonutChart(
					categories = categories,
					totalIncome = totalIncome,
					strokeWidth = 22.dp,
					gapDegrees = 2f,
					animProgress = animProgress,
					modifier = Modifier.fillMaxSize()
				)

				// Center label
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Text(
						text = AppCtx.currency.writer(totalIncome),
						color = colorScheme.onSurface,
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.SemiBold,
					)
					Text(
						text = "${stringResource(Res.string.template_page_income)} / ${stringResource(Res.string.template_page_month_abbreviated)}",
						color = colorScheme.onTertiaryContainer,
						textAlign = TextAlign.Center,
						style = MaterialTheme.typography.bodyMedium,
					)
				}
			}

			Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
				categories.forEach {
					val pct = if (totalIncome > 0) (it.amount / totalIncome * 100).toInt() else null
					LegendRow(category = it, percent = pct)
				}
			}
		}
	}
}

@Composable
private fun DonutChart(
	categories: List<BudgetCategory>,
	totalIncome: Long,
	strokeWidth: Dp,
	gapDegrees: Float,
	animProgress: Float,
	modifier: Modifier = Modifier,
) {
	val defaultColor = LocalFinanceColors.current.free
	Canvas(modifier = modifier) {
		val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
		val diameter = minOf(size.width, size.height)
		val topLeft = Offset(
			x = (size.width - diameter) / 2f,
			y = (size.height - diameter) / 2f,
		)
		val arcSize = Size(diameter, diameter)

		var startAngle = -90f
		val totalGap = gapDegrees * categories.size

		if (totalIncome > 0 && categories.none { it.amount < 0 }) {
			categories.forEach { category ->
				val fraction = (category.amount / totalIncome).toFloat()
				val sweep = (360f - totalGap) * fraction * animProgress

				drawArc(
					color = category.color,
					startAngle = startAngle,
					sweepAngle = sweep,
					useCenter = false,
					topLeft = topLeft,
					size = arcSize,
					style = stroke,
				)
				startAngle += sweep + gapDegrees
			}
		} else {
			drawArc(
				color = defaultColor,
				startAngle = startAngle,
				sweepAngle = 360f,
				useCenter = false,
				topLeft = topLeft,
				size = arcSize,
				style = stroke,
			)
		}

	}
}



@Composable
private fun LegendRow(
	category: BudgetCategory,
	percent: Int?,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
	) {
		Box(
			modifier = Modifier
				.size(10.dp)
				.background(category.color, CircleShape)
		)
		Text(
			text = category.label,
			color = colorScheme.onSurface,
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			modifier = Modifier.width(60.dp),
		)
		Text(
			text = "${percent ?: "-"}%",
			color = colorScheme.outline,
			style = MaterialTheme.typography.labelLarge,
			modifier = Modifier.width(36.dp),
			textAlign = TextAlign.End,
		)
		Text(
			text = AppCtx.currency.writer(category.amount),
			color = colorScheme.onTertiaryContainer,
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.SemiBold,
		)
	}
}
