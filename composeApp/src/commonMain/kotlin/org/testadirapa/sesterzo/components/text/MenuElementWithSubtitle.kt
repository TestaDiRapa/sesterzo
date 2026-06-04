package org.testadirapa.sesterzo.components.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuElementWithSubtitle(
	label: String,
	subtitle: String,
) {
	Column {
		Text(
			text = label,
			color = colorScheme.onSurface,
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(Modifier.height(4.dp))
		Text(
			text = subtitle,
			color = colorScheme.onSurfaceVariant,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}