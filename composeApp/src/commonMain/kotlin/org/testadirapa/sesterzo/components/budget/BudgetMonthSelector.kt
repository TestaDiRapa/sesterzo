package org.testadirapa.sesterzo.components.budget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.currentBudgetReference
import org.testadirapa.sesterzo.utils.monthName
import org.testadirapa.sesterzo.utils.nextReference
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_down
import sesterzo.composeapp.generated.resources.arrow_left
import sesterzo.composeapp.generated.resources.arrow_right
import sesterzo.composeapp.generated.resources.arrow_up
import sesterzo.composeapp.generated.resources.now
import sesterzo.composeapp.generated.resources.plus

@Composable
fun BudgetMonthSelector(
	budgetReference: BudgetReference,
	isExpanded: Boolean,
	onDateClick: () -> Unit,
	onNext: (() -> Unit)?,
	onPrev: (() -> Unit)?,
	onCreate: (reference: BudgetReference) -> Unit,
	modifier: Modifier,
) {
	val iconButtonsModifier = Modifier.size(24.dp)
	Card(
		modifier = modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(60.dp)
				.padding(horizontal = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			IconButton(
				onClick = onPrev ?: {},
				enabled = onPrev != null,
				colors = iconButtonColors(),
				shape = RoundedCornerShape(8.dp)
			) {
				Icon(
					modifier = iconButtonsModifier,
					painter = painterResource(Res.drawable.arrow_left),
					contentDescription = "Previous",
					tint = colorScheme.onSurfaceVariant,
				)
			}
			BudgetTitleWithButton(
				budgetReference = budgetReference,
				isExpanded = isExpanded,
				onDateClick = onDateClick,
			)
			if (onNext != null) {
				IconButton(
					onClick = onNext,
					colors = iconButtonColors(),
					shape = RoundedCornerShape(8.dp)
				) {
					Icon(
						modifier = iconButtonsModifier,
						painter = painterResource(Res.drawable.arrow_right),
						contentDescription = "Next",
						tint = colorScheme.onSurfaceVariant,
					)
				}
			} else {
				IconButton(
					onClick = { onCreate(budgetReference.nextReference()) },
					colors = IconButtonDefaults.iconButtonColors(
						containerColor = colorScheme.primaryContainer,
						contentColor = colorScheme.onPrimaryContainer,
					),
					shape = RoundedCornerShape(8.dp)
				) {
					Icon(
						modifier = iconButtonsModifier,
						painter = painterResource(Res.drawable.plus),
						contentDescription = "New budget",
						tint = colorScheme.onPrimaryContainer,
					)
				}
			}
		}
	}
}

@Composable
private fun BudgetTitleWithButton(
	budgetReference: BudgetReference,
	isExpanded: Boolean,
	onDateClick: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.clickable {
			onDateClick()
		}
	) {
		Text(
			text = "${monthName(budgetReference.month, abbreviated = false)} ${budgetReference.year}",
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
		if (currentBudgetReference().let { it.year == budgetReference.year && it.month == budgetReference.month }) {
			Spacer(Modifier.width(4.dp))
			Box(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 2.dp)
					.height(20.dp)
					.width(40.dp)
					.background(color = colorScheme.primary, shape = RoundedCornerShape(4.dp)),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = stringResource(Res.string.now),
					style = MaterialTheme.typography.bodySmall,
					fontWeight = FontWeight.Bold,
					color = colorScheme.onPrimary,
				)
			}
		}
		Icon(
			modifier = Modifier.height(18.dp).width(18.dp),
			painter = if (isExpanded) {
					painterResource(Res.drawable.arrow_up)
				} else {
					painterResource(Res.drawable.arrow_down)
				},
			contentDescription = "Show / Hide",
			tint = colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun iconButtonColors(): IconButtonColors = IconButtonDefaults.iconButtonColors(
	containerColor = colorScheme.surfaceVariant,
	contentColor = colorScheme.onSurfaceVariant,
	disabledContainerColor = colorScheme.surfaceContainerHigh,
)