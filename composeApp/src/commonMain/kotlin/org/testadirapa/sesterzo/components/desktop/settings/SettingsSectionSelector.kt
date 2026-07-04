package org.testadirapa.sesterzo.components.desktop.settings

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.components.ui.DesktopSelector
import org.testadirapa.sesterzo.components.ui.DesktopSelectorIcon
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right

@Composable
fun SettingsSectionSelector(
	label: String,
	isSelected: Boolean,
	painter: Painter,
	color: Color,
	onClick: () -> Unit,
) {
	DesktopSelector(
		isSelected = isSelected,
		onClick = onClick,
		startContent = {
			DesktopSelectorIcon(label, painter, color)
		},
		endContent = {
			Icon(
				modifier = Modifier.size(24.dp),
				painter = painterResource(Res.drawable.arrow_right),
				contentDescription = "Next",
				tint = colorScheme.onSurfaceVariant,
			)
		}
	)
}
