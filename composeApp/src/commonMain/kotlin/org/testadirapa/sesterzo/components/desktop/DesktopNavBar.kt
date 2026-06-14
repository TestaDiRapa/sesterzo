package org.testadirapa.sesterzo.components.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.space.DesktopSpaceSwitcher
import org.testadirapa.sesterzo.components.ui.LogoWithName
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.Page
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.bank
import sesterzo.composeapp.generated.resources.bottom_menu_budget_page
import sesterzo.composeapp.generated.resources.bottom_menu_history_page
import sesterzo.composeapp.generated.resources.bottom_menu_settings_page
import sesterzo.composeapp.generated.resources.bottom_menu_template_page
import sesterzo.composeapp.generated.resources.receipt
import sesterzo.composeapp.generated.resources.settings
import sesterzo.composeapp.generated.resources.template

@Composable
fun DesktopNavBar(
	space: Space,
	spaceThumbnail: Base64String?,
	currentPage: Page,
	onCreateSpace: (currentSpace: Space) -> Unit,
	onSwitchSpace: (Space) -> Unit,
	onPageChange: (newPage: Page) -> Unit,
	onError: (error: Throwable) -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight()
			.background(color = colorScheme.surface),
	) {
		Column(
			modifier = Modifier.padding(16.dp),
		) {
			LogoWithName()
			Spacer(modifier = Modifier.size(16.dp))
			DesktopSpaceSwitcher(
				space = space,
				spaceThumbnail = spaceThumbnail,
				onSelect = onSwitchSpace,
				onCreate = { onCreateSpace(space) },
				onJoin = {},
				onError = onError,
			)
			Spacer(modifier = Modifier.size(16.dp))
			HorizontalDivider(color = colorScheme.outline)
			Spacer(modifier = Modifier.size(16.dp))
			NavigationButton(
				icon = painterResource(Res.drawable.bank),
				label = stringResource(Res.string.bottom_menu_budget_page),
				isSelected = currentPage == Page.Budget,
				onClick = { onPageChange(Page.Budget) }
			)
			Spacer(modifier = Modifier.size(8.dp))
			NavigationButton(
				icon = painterResource(Res.drawable.receipt),
				label = stringResource(Res.string.bottom_menu_history_page),
				isSelected = currentPage == Page.Entries,
				onClick = { onPageChange(Page.Entries) }
			)
			Spacer(modifier = Modifier.size(8.dp))
			NavigationButton(
				icon = painterResource(Res.drawable.template),
				label = stringResource(Res.string.bottom_menu_template_page),
				isSelected = currentPage == Page.Template,
				onClick = { onPageChange(Page.Template) }
			)
			Spacer(modifier = Modifier.size(8.dp))
			NavigationButton(
				icon = painterResource(Res.drawable.settings),
				label = stringResource(Res.string.bottom_menu_settings_page),
				isSelected = currentPage == Page.Settings,
				onClick = { onPageChange(Page.Settings) }
			)
		}
	}
}

@Composable
fun NavigationButton(
	icon: Painter,
	label: String,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier.clickable(onClick = onClick),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) colorScheme.surfaceVariant else colorScheme.surface,
		),
	) {
		Row(
			modifier = Modifier.padding(8.dp).fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp),
		) {
			Icon(
				modifier = Modifier.size(24.dp),
				painter = icon,
				contentDescription = null,
				tint = if (isSelected) { colorScheme.primary } else { colorScheme.onTertiary },
			)
			Text(
				text = label,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold,
				color = if (isSelected) { colorScheme.onBackground } else { colorScheme.onTertiary },
			)
		}
	}
}