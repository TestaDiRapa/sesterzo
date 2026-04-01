package org.testadirapa.sesterzo.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TitleAndSubtitle(
	title: String,
	subtitle: String,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp),
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
		Text(
			text = subtitle,
			style = MaterialTheme.typography.titleMedium,
			color = colorScheme.onSurfaceVariant,
		)
	}
}