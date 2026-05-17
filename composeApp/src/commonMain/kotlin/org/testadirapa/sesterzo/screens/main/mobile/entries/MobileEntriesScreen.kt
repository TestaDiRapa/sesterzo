package org.testadirapa.sesterzo.screens.main.mobile.entries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.entries.EntryTypeBadge
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.styles.typography.amountTextStyleMedium
import org.testadirapa.sesterzo.utils.dayName
import org.testadirapa.sesterzo.utils.entryTypeColor
import org.testadirapa.sesterzo.utils.monthName
import org.testadirapa.sesterzo.utils.toLocalDateTime
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.entry_list_page_today
import sesterzo.composeapp.generated.resources.entry_list_page_yesterday
import kotlin.time.Clock

@Composable
fun MobileEntriesScreen(
	scaffoldPadding: PaddingValues,
	entries: List<DecryptedEntry>
) {
	val activeEntriesByDay = entries
		.filterNot { it.deleted }
		.groupBy { it.updated.toLocalDateTime().day }
	Column(
		modifier = Modifier
			.padding(scaffoldPadding)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Spacer(Modifier.height(8.dp))
		activeEntriesByDay.forEach { (_, entriesInDay) ->
			val date = entriesInDay.first().updated.toLocalDateTime().date
			SectionDateDivider(
				date = date,
			)
			entriesInDay.forEach { entry ->
				EntryCard(entry = entry)
			}
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
fun EntryCard(
	entry: DecryptedEntry,
) {
//	var user by remember { mutableStateOf<User?>(null) }
//	LaunchedEffect(entry) {
//		runCatching {
//			user = AppCtx.api.user
//		}
//	}
	Card(
		modifier = Modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
	) {
		Row(
			modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Box(
				modifier = Modifier
					.fillMaxHeight()
					.width(6.dp)
					.background(entryTypeColor(entry.type), RoundedCornerShape(6.dp))
			)
			Spacer(Modifier.width(8.dp))
			Column(
				modifier = Modifier.padding(horizontal = 12.dp)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 12.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween,
				) {
					Column {
						Text(
							text = entry.label,
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.SemiBold,
							color = colorScheme.onSurface,
						)
						entry.description?.also { description ->
							Spacer(Modifier.height(8.dp))
							Text(
								text = description,
								style = MaterialTheme.typography.bodyMedium,
								color = colorScheme.onSurfaceVariant,
							)
						}
					}
					Text(
						text = AppCtx.currency.writer(entry.amount),
						color = colorScheme.onSurface,
						style = amountTextStyleMedium()
					)
				}
				HorizontalDivider(color = colorScheme.outline)
				Row(
					modifier = Modifier.padding(8.dp),
				) {
					EntryTypeBadge(
						entryType = entry.type,
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = "User",
						style = MaterialTheme.typography.bodyMedium,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.primary,
					)
				}
			}
		}
	}
}

@Composable
fun SectionDateDivider(
	date: LocalDate
) {
	val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
	Row(
		modifier = Modifier.fillMaxWidth(),
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
}