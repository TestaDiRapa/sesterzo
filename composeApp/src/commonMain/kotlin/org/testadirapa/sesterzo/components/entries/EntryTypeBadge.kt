package org.testadirapa.sesterzo.components.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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

@Composable
fun EntryTypeBadge(
	entryType: Entry.EntryType,
	modifier: Modifier = Modifier,
) {
	val (backgroundColor, contentColor) = when (entryType) {
		Entry.EntryType.Expense -> colorScheme.onError.copy(alpha = 0.30f) to colorScheme.error
		Entry.EntryType.Income -> colorScheme.primaryContainer to colorScheme.onPrimaryContainer
		Entry.EntryType.Saving -> LocalFinanceColors.current.savedBg.copy(alpha = 0.30f) to LocalFinanceColors.current.saved
	}
	val label = when (entryType) {
		Entry.EntryType.Expense -> stringResource(Res.string.add_entry_form_type_expense).uppercase()
		Entry.EntryType.Income -> stringResource(Res.string.add_entry_form_type_income).uppercase()
		Entry.EntryType.Saving -> stringResource(Res.string.add_entry_form_type_saving).uppercase()
	}
	val icon = when (entryType) {
		Entry.EntryType.Expense -> painterResource(Res.drawable.arrow_back)
		Entry.EntryType.Income -> painterResource(Res.drawable.arrow_forward)
		Entry.EntryType.Saving -> painterResource(Res.drawable.banknotes)
	}
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
			painter = icon,
			contentDescription = null,
			tint = contentColor,
		)
		Text(
			text = label,
			color = contentColor,
			style = TextStyle(
				fontSize = 9.5.sp,
				fontWeight = FontWeight.SemiBold,
				letterSpacing = 0.4.sp,
			),
		)
	}
}