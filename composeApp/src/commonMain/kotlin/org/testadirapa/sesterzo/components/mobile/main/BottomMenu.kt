package org.testadirapa.sesterzo.components.mobile.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun BottomMenu(
	currentPage: Page,
	onPageChange: (newPage: Page) -> Unit
) {
	Column(
		modifier = Modifier.padding(bottom = 8.dp)
	) {
		HorizontalDivider()
		Spacer(Modifier.fillMaxWidth().height(8.dp))
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier
				.fillMaxWidth()
		) {
			MenuIcon(
				painter = painterResource(Res.drawable.bank),
				name = stringResource(Res.string.bottom_menu_budget_page),
				isSelected = currentPage == Page.Budget,
				onClick = { onPageChange(Page.Budget) }
			)
			MenuIcon(
				painter = painterResource(Res.drawable.receipt),
				name = stringResource(Res.string.bottom_menu_history_page),
				isSelected = currentPage == Page.Entries,
				onClick = { onPageChange(Page.Entries) }
			)
			MenuIcon(
				painter = painterResource(Res.drawable.template),
				name = stringResource(Res.string.bottom_menu_template_page),
				isSelected = currentPage == Page.Template,
				onClick = { onPageChange(Page.Template) }
			)
			MenuIcon(
				painter = painterResource(Res.drawable.settings),
				name = stringResource(Res.string.bottom_menu_settings_page),
				isSelected = currentPage == Page.Settings,
				onClick = { onPageChange(Page.Settings) }
			)
		}
	}
}

@Composable
private fun MenuIcon(
	painter: Painter,
	name: String,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.clickable(onClick = onClick)
	) {
		Icon(
			modifier = Modifier.size(30.dp),
			painter = painter,
			contentDescription = null,
			tint = if (isSelected) { colorScheme.primary } else { colorScheme.onTertiary },
		)
		Text(
			text = name,
			style = MaterialTheme.typography.bodySmall,
			fontWeight = FontWeight.Bold,
			color = if (isSelected) { colorScheme.primary } else { colorScheme.onTertiary },
		)
	}
}