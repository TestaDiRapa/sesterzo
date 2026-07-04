package org.testadirapa.sesterzo.components.desktop.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.components.text.MenuElementWithSubtitle
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right

@Composable
fun SettingsRow(
	label: String,
	subtitle: String,
	onClick: () -> Unit,
	showIcon: Boolean = true,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.clickable(onClick = onClick),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = if (showIcon) Arrangement.Start else Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Spacer(Modifier.width(4.dp))
			Box(
				modifier = Modifier
					.width(5.dp)
					.height(40.dp)
					.background(colorScheme.primary, RoundedCornerShape(5.dp))
			)
			Spacer(Modifier.width(16.dp))
			MenuElementWithSubtitle(
				label = label,
				subtitle = subtitle,
			)
		}
		if (showIcon) {
			Icon(
				modifier = Modifier.size(24.dp),
				painter = painterResource(Res.drawable.arrow_right),
				contentDescription = "Next",
				tint = colorScheme.onSurfaceVariant,
			)
		}
	}
}