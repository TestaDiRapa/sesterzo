package org.testadirapa.sesterzo.components.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.utils.dayName
import org.testadirapa.sesterzo.utils.monthName
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.entries
import sesterzo.composeapp.generated.resources.entry
import sesterzo.composeapp.generated.resources.entry_list_page_today
import sesterzo.composeapp.generated.resources.entry_list_page_yesterday
import kotlin.time.Clock

@Composable
fun EntrySectionDateDivider(
	date: LocalDate,
	numEntries: Int?,
	modifier: Modifier = Modifier,
) {
	val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = if (numEntries == null) Arrangement.Start else Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.Bottom,
		) {
			when {
				date == today -> {
					Text(
						text = stringResource(Res.string.entry_list_page_today),
						fontSize = 17.sp,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.onSurface,
					)
				}
				date.plus(1, DateTimeUnit.DAY) == today -> {
					Text(
						text = stringResource(Res.string.entry_list_page_yesterday),
						fontSize = 17.sp,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.onSurface,
					)
				}
				else -> {
					Text(
						text = "${monthName(date.month, abbreviated = true)} ${date.day}",
						fontSize = 17.sp,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.onSurface,
					)
				}
			}
			if (date == today || date.plus(1, DateTimeUnit.DAY) == today) {
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text =  "${monthName(date.month, abbreviated = true)} ${date.day}",
					style = MaterialTheme.typography.labelMedium,
					color = colorScheme.onSurfaceVariant,
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text =  "·",
					style = MaterialTheme.typography.labelMedium,
					color = colorScheme.onSurfaceVariant,
				)
			}
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = dayName(date.dayOfWeek, abbreviated = true),
				style = MaterialTheme.typography.labelMedium,
				color = colorScheme.onSurfaceVariant,
			)
		}
		if (numEntries != null) {
			Text(
				text = buildString {
					append(numEntries)
					append(' ')
					if (numEntries == 1) {
						append(stringResource(Res.string.entry))
					} else {
						append(stringResource(Res.string.entries))
					}
				},
				style = MaterialTheme.typography.labelMedium,
				color = colorScheme.onSurfaceVariant,
			)
		}
	}
}