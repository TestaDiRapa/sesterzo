package org.testadirapa.sesterzo.screens.main.desktop.entries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.entries.EntryScheduledBadge
import org.testadirapa.sesterzo.components.entries.EntrySectionDateDivider
import org.testadirapa.sesterzo.components.entries.EntryTypeBadge
import org.testadirapa.sesterzo.components.entries.EntryTypeIcon
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import org.testadirapa.sesterzo.styles.typography.amountTextStyleVeryLarge
import org.testadirapa.sesterzo.utils.currentLocalDate
import org.testadirapa.sesterzo.utils.dayName
import org.testadirapa.sesterzo.utils.groupActiveByDay
import org.testadirapa.sesterzo.utils.toLocalDate
import org.testadirapa.sesterzo.utils.toLocalDateTime
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right
import sesterzo.composeapp.generated.resources.entry_list_page_created
import sesterzo.composeapp.generated.resources.entry_list_page_created_by
import sesterzo.composeapp.generated.resources.entry_list_page_delete_emtry
import sesterzo.composeapp.generated.resources.entry_list_page_when
import sesterzo.composeapp.generated.resources.trash

@Composable
fun DesktopEntryScreen(
	entries: List<DecryptedEntry>,
	onDeleteEntry: (entryId: String) -> Unit,
) {
	var usersById by remember { mutableStateOf<Map<String, User>>(emptyMap()) }
	var selectedEntry by remember { mutableStateOf<DecryptedEntry?>(null) }
	LaunchedEffect(entries.size) {
		usersById = AppCtx.api.user.getUsers(
			userIds = entries.mapTo(mutableSetOf()) { it.createdBy }.toList(),
			bypassCache = false
		).associateBy { it.id }
	}
	Row(
		modifier = Modifier.fillMaxSize(),
	) {
		Column(
			modifier = Modifier
				.weight(2f)
				.verticalScroll(rememberScrollState())
		) {
			Spacer(Modifier.height(8.dp))
			entries.groupActiveByDay().forEach { (_, entriesInDay) ->
				EntrySectionDateDivider(
					date = entriesInDay.first().date,
					numEntries = entriesInDay.size,
					modifier = Modifier.padding(horizontal = 16.dp)
				)
				Spacer(Modifier.height(8.dp))
				entriesInDay.forEachIndexed { index, entry ->
					if (index > 0) {
						HorizontalDivider(color = colorScheme.outline)
					}
					key(entry.id) {
						EntryRow(
							entry = entry,
							isSelected = selectedEntry == entry,
							onClick = { selectedEntry = entry },
						)
					}
				}
				Spacer(Modifier.height(8.dp))
			}
		}
		VerticalDivider(color = colorScheme.outline)
		Column(
			modifier = Modifier
				.padding(all = 16.dp)
				.weight(1f)
		) {
			selectedEntry?.also { entry ->
				EntryDisplay(
					entry = entry,
					user = usersById[entry.createdBy],
					onDelete = {
						onDeleteEntry(entry.id)
						selectedEntry = null
					}
				)
			}
		}
	}
}

@Composable
private fun EntryDisplay(
	entry: DecryptedEntry,
	user: User?,
	onDelete: () -> Unit,
) {
	val entryUpdateDateTime = entry.updated.toLocalDateTime()
	Column {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {
			EntryTypeBadge(
				entryType = entry.type,
			)
			if (entry.updated.toLocalDate() > currentLocalDate()) {
				Spacer(Modifier.width(12.dp))
				EntryScheduledBadge()
			}
		}
		Spacer(Modifier.height(16.dp))
		Text(
			text = AppCtx.currency.writer(entry.amount),
			color = colorScheme.onSurface,
			style = amountTextStyleVeryLarge()
		)
		Text(
			text = entry.label,
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.SemiBold,
			color = colorScheme.onSurface,
		)
		entry.description?.also { description ->
			Spacer(Modifier.height(16.dp))
			Card(
				border = BorderStroke(width = 1.dp, color = colorScheme.outline),
				colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
			) {
				Row(
					modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
				) {
					Text(
						text = description,
						style = MaterialTheme.typography.bodyLarge,
						color = colorScheme.onSurfaceVariant,
					)
				}
			}
		}
		Spacer(Modifier.height(16.dp))
		EntryDisplayDetailsRow(
			key = stringResource(Res.string.entry_list_page_created_by),
			value = user?.name ?: "???",
		)
		EntryDisplayDetailsRow(
			key = stringResource(Res.string.entry_list_page_when),
			value = buildString {
				append(dayName(entry.date.dayOfWeek, abbreviated = true))
				append(" ")
				append(entry.date.day)
			}
		)
		EntryDisplayDetailsRow(
			key = stringResource(Res.string.entry_list_page_created),
			value = buildString {
				append(dayName(entryUpdateDateTime.dayOfWeek, abbreviated = true))
				append(" ")
				append(entryUpdateDateTime.day)
				append(", ")
				append("${entryUpdateDateTime.hour}:${entryUpdateDateTime.minute}")
			}
		)
		Spacer(Modifier.height(16.dp))
		Card(
			border = BorderStroke(width = 2.dp, color = colorScheme.error),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background),
			modifier = Modifier.clickable(onClick = onDelete)
		) {
			Row(
				modifier = Modifier.padding(all = 16.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Icon(
					painter = painterResource(Res.drawable.trash),
					contentDescription = "Delete",
					tint = colorScheme.error,
					modifier = Modifier.padding(end = 20.dp).size(24.dp),
				)
				Text(
					text = stringResource(Res.string.entry_list_page_delete_emtry),
					style = MaterialTheme.typography.bodyLarge,
					color = colorScheme.error,
				)
			}
		}
	}
}

@Composable
private fun EntryDisplayDetailsRow(
	key: String,
	value: String
) {
	Column{
		Spacer(Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Text(
				text = key,
				style = MaterialTheme.typography.labelLarge,
				color = colorScheme.onSurfaceVariant,
			)
			Text(
				text = value,
				style = MaterialTheme.typography.labelLarge,
				color = colorScheme.onSurfaceVariant,
			)
		}
		Spacer(Modifier.height(8.dp))
		HorizontalDivider(color = colorScheme.outline)
	}
}

@Composable
private fun EntryRow(
	entry: DecryptedEntry,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	val color = when(entry.type) {
		Entry.EntryType.Expense -> LocalFinanceColors.current.spent
		Entry.EntryType.Income -> colorScheme.primary
		Entry.EntryType.Saving -> LocalFinanceColors.current.saved
	}
	Row(
		modifier = Modifier
			.background(
				if (isSelected) {
					colorScheme.surfaceVariant
				} else {
					colorScheme.surface
				}
			)
			.clickable(onClick = onClick)
			.fillMaxWidth(),
	) {
		Row(
			modifier = Modifier
				.padding(all = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(12.dp),
			) {
				Box(
					modifier = Modifier
						.width(5.dp)
						.height(40.dp)
						.background(color, RoundedCornerShape(5.dp))
				)
				EntryTypeIcon(entryType = entry.type)
				Text(
					text = entry.label,
					style = MaterialTheme.typography.bodyLarge,
					fontWeight = FontWeight.SemiBold,
					color = colorScheme.onSurface,
				)
			}

			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(12.dp),
			) {
				Text(
					text = AppCtx.currency.writer(entry.amount),
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
}