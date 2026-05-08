package org.testadirapa.sesterzo.components.template

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.models.BudgetCategory
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.app_name
import sesterzo.composeapp.generated.resources.arrow_right
import sesterzo.composeapp.generated.resources.template_page_expenses
import sesterzo.composeapp.generated.resources.template_page_income_sources
import sesterzo.composeapp.generated.resources.template_page_savings

@Composable
fun TemplateUpdateMenu(
	incomeSources: DecryptedBudgetElement,
	expenses: DecryptedBudgetElement,
	savings: DecryptedBudgetElement,
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		TemplateRow(
			label = stringResource(Res.string.template_page_income_sources),
			subtitle = "${incomeSources.elements.size} sources",
			value = incomeSources.elements.values.sum(),
			color = colorScheme.primary,
		)
		HorizontalDivider(color = colorScheme.outline)
		TemplateRow(
			label = stringResource(Res.string.template_page_expenses),
			subtitle = "${expenses.elements.size} sources",
			value = expenses.elements.values.sum(),
			color = LocalFinanceColors.current.spent,
		)
		HorizontalDivider(color = colorScheme.outline)
		TemplateRow(
			label = stringResource(Res.string.template_page_savings),
			subtitle = "${savings.elements.size} sources",
			value = savings.elements.values.sum(),
			color = LocalFinanceColors.current.saved,
		)
	}
}

@Composable
private fun TemplateRow(
	label: String,
	subtitle: String,
	value: Amount,
	color: Color,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Spacer(Modifier.width(8.dp))
			Box(
				modifier = Modifier
					.width(5.dp)
					.height(40.dp)
					.background(color, RoundedCornerShape(5.dp))
			)
			Spacer(Modifier.width(8.dp))
			Column {
				Text(
					text = label,
					color = colorScheme.onSurface,
					style = MaterialTheme.typography.bodyLarge
				)
				Spacer(Modifier.height(4.dp))
				Text(
					text = subtitle,
					color = colorScheme.onSurfaceVariant,
					style = MaterialTheme.typography.bodyMedium
				)
			}
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