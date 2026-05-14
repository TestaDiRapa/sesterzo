package org.testadirapa.sesterzo.components.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModalTitle(
	subtitle: String,
	subtitleSpec: String?,
	title: String,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier) {
		Text(
			text = "$subtitle${subtitleSpec?.let { " · $it" } ?: ""}",
			style = MaterialTheme.typography.labelSmall,
			color = colorScheme.onSurfaceVariant,
		)
		Spacer(Modifier.height(4.dp))
		Text(
			text = title,
			fontSize = 17.sp,
			fontWeight = FontWeight.SemiBold,
			color = colorScheme.onSurface,
		)
	}
}