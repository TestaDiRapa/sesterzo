package org.testadirapa.sesterzo.components.mobile.budget

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.utils.currentDate
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.app_name
import sesterzo.composeapp.generated.resources.april
import sesterzo.composeapp.generated.resources.arrow_down
import sesterzo.composeapp.generated.resources.arrow_up
import sesterzo.composeapp.generated.resources.august
import sesterzo.composeapp.generated.resources.content_copy_icon
import sesterzo.composeapp.generated.resources.december
import sesterzo.composeapp.generated.resources.february
import sesterzo.composeapp.generated.resources.january
import sesterzo.composeapp.generated.resources.july
import sesterzo.composeapp.generated.resources.june
import sesterzo.composeapp.generated.resources.march
import sesterzo.composeapp.generated.resources.may
import sesterzo.composeapp.generated.resources.november
import sesterzo.composeapp.generated.resources.now
import sesterzo.composeapp.generated.resources.october
import sesterzo.composeapp.generated.resources.september

@Composable
fun BudgetSelector(
	date: LocalDate,
	onDateClick: () -> Unit,
) {
	Card(
		modifier = Modifier
			.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Row(
			modifier = Modifier.fillMaxWidth().height(60.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceEvenly,
		) {
			Text("next")
			BudgetTitleWithButton(
				date = date,
				onDateClick = onDateClick,
			)
			Text("previous")
		}
	}
}

@Composable
private fun BudgetTitleWithButton(
	date: LocalDate,
	onDateClick: () -> Unit,
) {
	var isExpanded by remember { mutableStateOf(false) }
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.clickable {
			isExpanded = !isExpanded
			onDateClick()
		}
	) {
		Text(
			text = "${monthName(date.month)} ${date.year}",
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
		if (currentDate().let { it.year == date.year && it.month == date.month }) {
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
private fun monthName(month: Month): String = when (month) {
	Month.JANUARY -> stringResource(Res.string.january)
	Month.FEBRUARY -> stringResource(Res.string.february)
	Month.MARCH -> stringResource(Res.string.march)
	Month.APRIL -> stringResource(Res.string.april)
	Month.MAY -> stringResource(Res.string.may)
	Month.JUNE -> stringResource(Res.string.june)
	Month.JULY -> stringResource(Res.string.july)
	Month.AUGUST -> stringResource(Res.string.august)
	Month.SEPTEMBER -> stringResource(Res.string.september)
	Month.OCTOBER -> stringResource(Res.string.october)
	Month.NOVEMBER -> stringResource(Res.string.november)
	Month.DECEMBER -> stringResource(Res.string.december)
}