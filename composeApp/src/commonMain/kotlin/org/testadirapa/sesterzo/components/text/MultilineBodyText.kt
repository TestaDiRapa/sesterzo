package org.testadirapa.sesterzo.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MultilineBodyText(
	resources: List<StringResource>
) {
	resources.forEach { resource ->
		Text(
			text = stringResource(resource),
			style = MaterialTheme.typography.bodyMedium,
			color = colorScheme.onSurfaceVariant,
		)
	}
}