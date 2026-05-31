package org.testadirapa.sesterzo.components.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_entry_form_type_expense
import sesterzo.composeapp.generated.resources.add_entry_form_type_income
import sesterzo.composeapp.generated.resources.add_entry_form_type_saving
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.arrow_forward
import sesterzo.composeapp.generated.resources.banknotes
import sesterzo.composeapp.generated.resources.schedule
import sesterzo.composeapp.generated.resources.scheduled

@Composable
fun EntryScheduledBadge(
	modifier: Modifier = Modifier,
) {
	val backgroundColor = colorScheme.primaryContainer
	val contentColor = colorScheme.onPrimaryContainer
	Row(
		modifier = modifier
			.background(backgroundColor, RoundedCornerShape(4.dp))
			.border(1.dp, contentColor, RoundedCornerShape(4.dp))
			.padding(start = 5.dp, top = 2.dp, end = 6.dp, bottom = 2.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = CenterVertically,
	) {
		Icon(
			modifier = Modifier.size(14.dp),
			painter = painterResource(Res.drawable.schedule),
			contentDescription = null,
			tint = contentColor,
		)
		Text(
			text = stringResource(Res.string.scheduled).uppercase(),
			color = contentColor,
			style = TextStyle(
				fontSize = 9.5.sp,
				fontWeight = FontWeight.SemiBold,
				letterSpacing = 0.4.sp,
			),
		)
	}
}