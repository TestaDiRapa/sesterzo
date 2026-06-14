package org.testadirapa.sesterzo.components.template

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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.text.MenuElementWithSubtitle
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right
import sesterzo.composeapp.generated.resources.template_page_expenses
import sesterzo.composeapp.generated.resources.template_page_income_sources
import sesterzo.composeapp.generated.resources.template_page_savings

@Composable
fun TemplateUpdateMenu(
	incomeSources: DecryptedBudgetElement,
	expenses: DecryptedBudgetElement,
	savings: DecryptedBudgetElement,
	onFormOpen: () -> Unit,
	setTemplate: (Pair<String, DecryptedBudgetElement>) -> Unit,
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		val options = listOf(
			Triple(stringResource(Res.string.template_page_income_sources), incomeSources, colorScheme.primary),
			Triple(stringResource(Res.string.template_page_expenses), expenses, LocalFinanceColors.current.spent),
			Triple(stringResource(Res.string.template_page_savings), savings, LocalFinanceColors.current.saved),
		)
		options.forEachIndexed { index, (label, sources, color) ->
			TemplateRow(
				label = label,
				subtitle = "${sources.elements.size} sources",
				value = sources.elements.values.sum(),
				color = color,
				onClick = {
					setTemplate(label to sources)
					onFormOpen()
				}
			)
			if (index != options.lastIndex) {
				HorizontalDivider(color = colorScheme.outline)
			}
		}
	}
}

@Composable
private fun TemplateRow(
	label: String,
	subtitle: String,
	value: Amount,
	color: Color,
	onClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.clickable(onClick = onClick),
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
					.height(40.dp)
					.background(color, RoundedCornerShape(5.dp))
			)
			Spacer(Modifier.width(16.dp))
			MenuElementWithSubtitle(
				label = label,
				subtitle = subtitle,
			)
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				text = AppCtx.currency.writer(value),
				color = colorScheme.onSurface,
				style = amountTextStyleLarge()
			)
			Icon(
				modifier = Modifier.size(24.dp),
				painter = painterResource(Res.drawable.arrow_right),
				contentDescription = "Next",
				tint = colorScheme.onSurfaceVariant,
			)
		}
	}
}