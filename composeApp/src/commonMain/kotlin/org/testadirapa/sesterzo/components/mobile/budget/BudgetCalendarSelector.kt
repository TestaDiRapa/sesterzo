package org.testadirapa.sesterzo.components.mobile.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Month
import kotlinx.datetime.number
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.monthName

@Composable
fun BudgetCalendarSelector(
	spaceId: String,
	currentBudget: BudgetReference,
	onError: (e: Throwable) -> Unit,
) {
	var year by remember { mutableStateOf(currentBudget.year) }
	var budgetsByMonth by remember { mutableStateOf<Map<Int, DecryptedBudget>>(emptyMap()) }
	LaunchedEffect(Unit) {
		budgetsByMonth = runCatching {
			AppCtx.api.budget.getBudgetsInSpaceForYear(
				spaceId = spaceId,
				year = currentBudget.year,
				bypassCache = false
			).associateBy { it.month }
		}.onFailure {
			onError(it)
		}.getOrNull() ?: emptyMap()
	}
	Card(
		modifier = Modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceEvenly,
			) {
				Text(
					text = "$year",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold,
					color = colorScheme.onBackground,
				)
				Spacer(Modifier.width(70.dp))
				Spacer(Modifier.width(70.dp))
				Spacer(Modifier.width(70.dp))
			}
			Spacer(Modifier.height(16.dp))
			MonthsGrid(
				currentBudget = currentBudget,
				budgetsByMonth = budgetsByMonth
			)
			Spacer(Modifier.height(16.dp))
			HorizontalDivider()
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceEvenly,
			) {
				Button(
					onClick = { },
					colors = ButtonColors(
						containerColor = colorScheme.surface,
						contentColor = colorScheme.onSurface,
						disabledContainerColor = colorScheme.surface,
						disabledContentColor = colorScheme.onSurface,
					)
				) {
					Text(
						text = "< ${year - 1}",
						style = MaterialTheme.typography.bodyMedium,
						color = colorScheme.onSurface,
					)
				}
				Spacer(Modifier.width(60.dp))
				Spacer(Modifier.width(60.dp))
				Button(
					onClick = { },
					colors = ButtonColors(
						containerColor = colorScheme.surface,
						contentColor = colorScheme.onSurface,
						disabledContainerColor = colorScheme.surface,
						disabledContentColor = colorScheme.onSurface,
					)
				) {
					Text(
						text = "${year + 1} >",
						style = MaterialTheme.typography.bodyMedium,
						color = colorScheme.onSurface,
					)
				}
			}
		}
	}
}

@Composable
private fun MonthsGrid(
	currentBudget: BudgetReference,
	budgetsByMonth: Map<Int, DecryptedBudget>,
) {
	Column{
		Month.entries.chunked(4).forEach { monthEntries ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceEvenly,
			) {
				monthEntries.forEach { month ->
					MonthCard(
						month = month,
						isBudgetPresent = budgetsByMonth.contains(month.number),
						isCurrentlySelected = currentBudget.month == month
					)
				}
			}
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun MonthCard(
	month: Month,
	isBudgetPresent: Boolean,
	isCurrentlySelected: Boolean,
) {
	val outlineColor = colorScheme.outline
	Card(
		modifier = Modifier
			.width(70.dp)
			.height(45.dp)
			.drawBehind {
				if (!isBudgetPresent) {
					drawRoundRect(
						color = outlineColor,
						style = Stroke(
							width = 1.dp.toPx(),
							pathEffect = PathEffect.dashPathEffect(
								intervals = floatArrayOf(6f, 6f), // dash length, gap length
								phase = 0f
							)
						),
						cornerRadius = CornerRadius(12.dp.toPx())
					)
				}
			},
		colors = when {
			isCurrentlySelected -> CardDefaults.cardColors(containerColor = colorScheme.primary)
			isBudgetPresent -> CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
			else -> CardDefaults.cardColors(containerColor = colorScheme.surface)
		},
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
			modifier = Modifier.fillMaxSize()
		) {
			Text(
				text = monthName(month, abbreviated = true),
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Bold,
				color = when {
					isCurrentlySelected -> colorScheme.onPrimary
					isBudgetPresent -> colorScheme.onSurface
					else -> colorScheme.outline
				},
			)
			if (isBudgetPresent) {
				Spacer(modifier = Modifier.height(4.dp))
				Box(
					modifier = Modifier
						.size(4.dp)
						.clip(CircleShape)
						.background(
							if (isCurrentlySelected) colorScheme.onPrimary else colorScheme.primary,
						)
				)
			}
		}
	}
}